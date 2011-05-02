(ns newsface.ranking
  (:use
   [newsface persistence youtube] :reload))


(def ^{:private true} *metrics*
     [:mutual :posters :commenters :photo-taggers :likers])


(def ^{:private true} *metrics-weights*
     [0.5 1 1 0.5 1])


(def ^{:private true} *metrics2weights*
     (zipmap *metrics* *metrics-weights*))


;;Represent a sequence of the strong tiers for the given user.
;;Let X be a user to rank, then rnk(X) is:
;;- Sum(for every metric) / metrics number."
(def *strong-tiers*
     (let [id2rank
	   (for [{name :name id :id} *friends*]
	     {:id id :count
	      (float (/ (reduce + (map #(* (get (fetch-one %1 :where {:id id}) :count)
					   (get *metrics2weights* %1)) *metrics*))
			(count *metrics*)))})
	   result (sort-by (fn [{id :id cnt :count}] cnt) id2rank)]
       (reverse result)))

(defn strong-tiers-video-kw
  "Retrieve the from the top boundary strong tiers
   the youtube videos tags"
  [boundary]
  (let [top-users-id (for [{id :id cnt :count} (take boundary *strong-tiers*)] id)]
    (println "TODo")))


(defn save-strong-tiers-video-kw
  "Stores inside the DB all the youtube video tags extracted from
   the wall."
  []
  (update-one :videos-tags))