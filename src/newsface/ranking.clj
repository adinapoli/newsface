(ns newsface.ranking
  (:use
   [newsface persistence youtube websites])
  (:require
   [clojure.contrib.logging :as log]
   [clojure.contrib.string :as str]))


(def ^{:private true} *metrics*
     [:mutual :posters :commenters :photo-taggers :likers])


(def ^{:private true} *metrics-weights*
     [0.5 1 1 0.5 1])


(def ^{:private true} *metrics2weights*
     (zipmap *metrics* *metrics-weights*))


;;Let X be a user to rank, then rnk(X) is:
;;  Sum(for every metric) / metrics number."
(defn rank-user
  [user-id]
  (float (/ (reduce + (map #(* (get (fetch-one %1 :where {:id user-id}) :count)
			       (get *metrics2weights* %1)) *metrics*))
	    (count *metrics*))))


;;Represent a sequence of the strong tiers for the given user.
(defn strong-tiers
  []
  (let [id2rank (for [{id :id name :name} @*friends*] {:id id :count (rank-user id)})
	result (sort-by (fn [{id :id cnt :count}] cnt) id2rank)]
    (reverse result)))


(def *boundary* 5)


(def *videos-keywords*
     (if @*current-user-id*
       (ref (fetch :videos-tags))
       (ref nil)))


(def *websites-keywords*
     (if @*current-user-id*
       (ref (fetch :websites-tags))
       (ref nil)))


(def *search-keywords*
     (if @*current-user-id*
       (ref (fetch :search-keywords :sort {:count -1}))
       (ref nil)))


(defn strong-tiers-video-kw
  "Retrieve the from the top boundary strong tiers
   the youtube videos tags"
  [boundary]
  (let [top-users-id (map :id (take boundary (strong-tiers)))]
    (flatten (map get-videos-tags top-users-id))))


(defn save-video-kw
  "Stores inside the DB all the youtube video tags extracted from
   the wall."
  []
  (update-one :videos-tags (strong-tiers-video-kw *boundary*)))


(defn strong-tiers-site-kw
  "Retrieve from the top boundary strong tiers
   the websites tags."
  [boundary]
  (let [top-users-id (map :id (take boundary (strong-tiers)))]
    (flatten (map get-websites-tags top-users-id))))




(defn save-website-kw
  "Stores inside the DB all the youtube video tags extracted from
   the wall."
  []
  (update-one :websites-tags (strong-tiers-site-kw *boundary*)))


;; Operations to perform
;; 1. Filter duplicates for each bunch of keywords
;; 2. put everything into lowercase
;; 3. Build an ordinated structure.
(defn find-search-keywords
  "The most important functions.
  It processes all the keywords (either video and from websites) and
  build a map of frequencies."
  []
  (let [video-kw-set (set (mapcat #(map str/lower-case (-> %1 :keywords)) @*videos-keywords*))
	sites-kw-set (set (mapcat #(map str/lower-case (-> %1 :keywords)) @*websites-keywords*))
	keywords (into (seq video-kw-set) (seq sites-kw-set))
	freq-map (frequencies keywords)]
    (for [entry freq-map] {:keyword (key entry) :count (val entry)})))


(defn save-search-keywords
  []
  (update-one :search-keywords (find-search-keywords)))


(defn get-search-keywords []
  @*search-keywords*)


(defn refetch-all-media
  "Refetches all the informations from the DB.
   Used after setting the token by the web app."
  []
  (do
    (log/info "Re-fetching keywords from DB..")
    (dosync (ref-set *videos-keywords* (fetch :videos-tags)))
    (println @*videos-keywords*)
    (dosync (ref-set *websites-keywords* (fetch :websites-tags)))
    (println @*websites-keywords*)
    (dosync (ref-set *search-keywords* (fetch :search-keywords :sort {:count -1})))
    (println @*search-keywords*)
    (log/info "Done.")))


(defn train-from-scratch
  "Helper function to train the user profile from scratch.
   It doesn't perform any check to see if some data is already present."
  []
  (do
    (log/info "Starting user profile training...")
    (log/info "Updating Facebook resources repository...")
    (update-repository)
    (log/info "Updating video keywords...")
    (save-video-kw)
    (log/info "Updating websites keywords...")
    (save-website-kw)
    (log/info "Extracting search keywords...")
    (save-search-keywords)
    (log/info "Done.")))