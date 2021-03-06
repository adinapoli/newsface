(ns newsface.persistence
  (:use
   [clj-facebook-graph.auth]
   [clojure.contrib json]
   [ring.util.codec :only [url-encode]] :reload)
  (:require
   [clj-facebook-graph [client :as client]]
   [clojure.contrib.logging :as log]
   [somnium [congomongo :as congo]]
   [net.cgrand.enlive-html :as en]))


;; I wanna try to use a NoSQL DB, and since Facebook as well as MongoDB
;; speaks the JSON language, this is the perfect environment to work in.

;; Database structure
;; :friends {:name :id}
;; :wall
;; :news-feed
;; :photos-tags
;; :mutual {:id :count}
;; :posters {:id :count}
;; :commenters {:id :count}
;; :photo-taggers {:id :count}
;; :friends-profiles
;; :likers
;; :videos-tags
;; :websites-tags


;;facebook-auth must be a map {:access-token token}
(def facebook-auth (ref nil))


;;The :db will be fetched/created based on the
;;current user of this FB session.
(def *current-user-id* (ref nil))


(def *my-user-profile* (ref nil))


(defn get-user-id
  []
  (with-facebook-auth @facebook-auth
    (client/get "https://graph.facebook.com/me" {:extract :id})))


(defn get-user-profile
  [user-id]
  (with-facebook-auth @facebook-auth
    (client/get (str "https://graph.facebook.com/" user-id) {:extract :body})))


(when @*current-user-id*
 (congo/mongo! :db (str "newsface-" @*current-user-id*)))


(def fetch congo/fetch)
(def fetch-count congo/fetch-count)
(def fetch-one congo/fetch-one)


(defn insert
  "Store a resource into a table, given the :table-name and
  the resource itself as a key-value pair."
  [table-name object]
  (congo/insert! table-name object))


(defn mass-insert
  "Store a sequence of objects inside a table."
  [table-name obj-seq]
  (congo/mass-insert! table-name obj-seq))


(defn fetch-resource
  "Fetch the given resource from facebook. The resource consists in a
   valid URL to fetch from."
  ([resource-url]
     (with-facebook-auth @facebook-auth
       (client/get resource-url {:extract :data})))
  
  ([resource-url fetch-limit]
     (take fetch-limit (with-facebook-auth @facebook-auth
			 (client/get resource-url {:query-parameters
						   {:limit fetch-limit}
						   :extract :data :paging true})))))



(defn get-all-friends []
  (fetch-resource "https://graph.facebook.com/me/friends"))


(def *friends*
     (if @*current-user-id*
       (ref (fetch :friends))
       (ref nil)))


(defn get-friend-name
  "Returns the name of the friend, given his id."
  [friend-id]
  (let [{id :id name :name} (fetch-one :friends :where {:id friend-id})]
    name))


(defn get-wall []
  (fetch-resource "https://graph.facebook.com/me/feed" 1000))


(def *wall*
     (if @*current-user-id*
       (ref (fetch :wall))
       (ref nil)))


(defn get-news-feed []
  (fetch-resource "https://graph.facebook.com/me/home" 1000))


(def *news-feed*
     (if @*current-user-id*
       (ref (fetch :news-feed))
       (ref nil)))


(defn get-all-photos-tags []
  (fetch-resource "https://graph.facebook.com/me/photos" 1000))


(def *photos-tags*
     (if @*current-user-id*
       (ref (fetch :photos-tags))
       (ref nil)))


(defn get-mutual-friends
  "Returns a map which contains the friends-id and the mutual friends count
   This method uses the old REST API."
  [friend-id]
  (let [access-token (get @facebook-auth :access-token)
	query (str "https://api.facebook.com"
		   "/method/friends.getMutualFriends?"
		   "format=JSON&"
		   "target_uid=" friend-id "&access_token=" access-token)]
    {:id friend-id :count (count (read-json (slurp query)))}))


(defn get-all-mutual-friends []
  (map get-mutual-friends (map :id @*friends*)))


(def *mutual-friends*
     (if @*current-user-id*
       (ref (fetch :mutual :sort {:count -1}))
       (ref nil)))


(defn get-wall-post-count
  "Returns a map which contains the friends-id and the number of post published
   on your wall."
  [friend-id]
  {:id friend-id :count (count (filter
				(fn [{name :name id :id}] (= id friend-id))
				(for [post @*wall*] (get-in post [:from]))))})


(defn get-all-wall-posters []
  (map get-wall-post-count (for [{name :name id :id} @*friends*] id)))


(def *wall-posters*
     (if @*current-user-id*
       (ref (fetch :posters :sort {:count -1}))
       (ref nil)))


(defn get-comment-count
  "Returns a map which contains the friends-id and the number of comments left
   on your wall."
  [friend-id]
  {:id friend-id :count (count
			 (filter (fn [{name :name id :id}] (= id friend-id))
				 ;;Nested for (sorry :S ). The inner for search
				 ;;for comments, the outer returns a map of maps {:name :id}
				 (for [entry
				       (flatten (for [entry @*wall*] (get-in entry [:comments :data])))]
				   (get-in entry [:from]))))})


(defn get-all-commenters []
  (map get-comment-count (for [{name :name id :id} @*friends*] id)))


(def *commenters*
     (if @*current-user-id*
       (ref (fetch :commenters :sort {:count -1}))
       (ref nil)))


(defn get-photo-tags-count
  "Returns a map which contains the friends-id and the number of tags
   about you in your photos. Memoized"
  [friend-id]
  {:id friend-id :count (count (filter
				(fn [{name :name id :id}] (= id friend-id))
				(for [photo @*photos-tags*] (-> photo :from))))})


(defn get-all-photo-taggers []
  (map get-photo-tags-count (for [{name :name id :id} @*friends*] id)))


(def *photo-taggers*
     (if @*current-user-id*
       (ref (fetch :photo-taggers :sort {:count -1}))
       (ref nil)))


(defn get-likes-count
  "Returns a map which contains the friends-id and the count of likes
   all over your wall."
  [friend-id]
  {:id friend-id :count (fetch-count :wall :where {"likes.data.id" friend-id})})


(defn get-all-likers []
  (map get-likes-count (for [{name :name id :id} @*friends*] id)))


(def *likers*
     (if @*current-user-id*
       (ref (fetch :likers :sort {:count -1}))
       (ref nil)))


(defn get-all-friends-profiles []
  (map get-user-profile (for [{name :name id :id} @*friends*] id)))


(def *friends-profiles*
     (if @*current-user-id*
       (ref (fetch :friends-profiles))
       (ref nil)))


(def ^{:private true} *tables*
     [:friends :wall :news-feed :photos-tags :mutual :posters
      :commenters :photo-taggers :likers :friends-profiles])


(def ^{:private true} *fetch-functions*
     [get-all-friends get-wall get-news-feed get-all-photos-tags
      get-all-mutual-friends get-all-wall-posters get-all-commenters
      get-all-photo-taggers get-all-likers get-all-friends-profiles])


(defn update-repository
  "You can use this function in order to refetch all the user information
   from the Facebook database. It can be a very slow process since several
   http requests are required."
  []
  (log/info "Updating repository, it can take a while...")
  (log/info "Deleting obsolete files...")
  (doseq [table *tables*] (congo/destroy! table {}))
  (log/info "Pushing new files...")
  (let [table2fn (merge {} (zipmap *tables* *fetch-functions*))]
    (doseq [table *tables*]
      (do
	(congo/mass-insert! table ((get table2fn table)))
	(log/info (str table " successfully populated."))))
    (log/info "Done.")))


(defn update-one
  "Use this if you want to update only one table."
  ([table-name]
     (log/info (str "Updating the " table-name " table..."))
     (log/info "Deleting obsolete files...")
     (congo/destroy! table-name {})
     (log/info "Pushing new files...")
     (let [table2fn (merge {} (zipmap *tables* *fetch-functions*))]
       (do
	 (congo/mass-insert! table-name ((get table2fn table-name)))
	 (log/info (str table-name " successfully populated.")))
       (log/info "Done.")))

  ([table-name data]
     (log/info (str "Updating the " table-name " table..."))
     (log/info "Deleting obsolete files...")
     (congo/destroy! table-name {})
     (log/info "Pushing new files...")
     (congo/mass-insert! table-name data)
     (log/info "Done.")))


(defn fql-fetch
  "Query the Facebook's servers trough the Facebook Query Language.
   It takes the query to submit and returns the results in JSON format."
  [query]
  (let [access-token (get @facebook-auth :access-token)
	query-url (str "https://api.facebook.com/method/fql.query?format=JSON"
		       "&query=" query "&access_token=" access-token)]
    (try
      (read-json (slurp query-url))
      (catch java.io.IOException e {:error_code 400}))))


(defn get-links-from
  [user-id]
  (let [fql-query "SELECT link_id,title,owner FROM link WHERE owner="]
    (loop [response {:error_code 1}]
      (if-not (get response :error_code)
	response
	(recur (fql-fetch (url-encode (str fql-query user-id))))))))


(defn get-links-urls-from
  [user-id]
  (let [fql-query "SELECT url FROM link WHERE owner="]
    (loop [response {:error_code 1}]
      (if-not (get response :error_code)
	response
	(recur (fql-fetch (url-encode (str fql-query user-id))))))))


(defn refetch-all
  "Refetches all the informations from the DB.
   Used after setting the token by the web app."
  []
  (do
    (congo/mongo! :db (str "newsface-" @*current-user-id*))
    (dosync (ref-set *friends* (fetch :friends)))
    (dosync (ref-set *wall* (fetch :wall)))
    (dosync (ref-set *news-feed* (fetch :news-feed)))
    (dosync (ref-set *photos-tags* (fetch :photos-tags)))
    (dosync (ref-set *mutual-friends* (fetch :mutual :sort {:count -1})))
    (dosync (ref-set *wall-posters* (fetch :posters :sort {:count -1})))
    (dosync (ref-set *commenters* (fetch :commenters :sort {:count -1})))
    (dosync (ref-set *photo-taggers* (fetch :photo-taggers :sort {:count -1})))
    (dosync (ref-set *likers* (fetch :likers :sort {:count -1})))
    (dosync (ref-set *friends-profiles* (fetch :friends-profiles)))))


(defn set-access-token
  [token]
  (do
    (log/info "Setting token..")
    (dosync (ref-set facebook-auth {:access-token (url-encode token)}))
    (dosync (ref-set *current-user-id* (get-user-id)))
    (dosync (ref-set *my-user-profile* (get-user-profile "me")))
    (log/info "Token set.")
    (log/info "Re-Fetching informations..")
    (refetch-all)
    (log/info "Done.")))