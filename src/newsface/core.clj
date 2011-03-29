(ns newsface.core
  (:use
   [clj-facebook-graph auth]
   [clojure.contrib json])
  (:require
   [clj-facebook-graph [client :as client]]))



(def *auth-token* "2227470867|2.HomsHqgDTl7W88AC68SQYQ__.3600.1301338800-1712326620|l6U7iXrJme_UEoljTomYMvKiMBc")


(def facebook-auth {:access-token *auth-token*})


(defn fetch-resource
  "Fetch the given resource from facebook. The resource consists in a
   valid URL to fetch from."
  ([resource-url]
     (with-facebook-auth facebook-auth
       (client/get resource-url {:extract :data})))
  
  ([resource-url fetch-limit]
     (take fetch-limit (with-facebook-auth facebook-auth
			 (client/get resource-url {:extract :data :paging true})))))


(def *friends* (fetch-resource "https://graph.facebook.com/me/friends"))


(def *wall* (fetch-resource "https://graph.facebook.com/me/feed" 1000))


(def *photos-tags* (fetch-resource "https://graph.facebook.com/me/photos" 1000))


(defn get-friend-name
  "Returns the name of the friend, given his id."
  [id]
  (let [result-lst (filter (fn [{n :name i :id}] (= i id)) *friends*)]
    (:name (first result-lst))))


(defn get-all-from
  [getter hmap]
  (do
    (doseq [{id :id name :name} *friends*] (getter id))
    @hmap))


(def *mutual-friends-map* (ref {}))
(defn get-mutual-friends
  "Returns a map which contains the friends-id and the mutual friends with the
   current user. This method used the old REST API. Memoized."
  [friend-id]
  (let [query (str "https://api.facebook.com"
		   "/method/friends.getMutualFriends?"
		   "format=JSON&"
		   "target_uid=" friend-id "&access_token=" *auth-token*)
	previous (get @*mutual-friends-map* friend-id)]
    (or previous
	(do (dosync (alter *mutual-friends-map* assoc friend-id (read-json (slurp query))))
	    (get @*mutual-friends-map* friend-id)))))


(defn get-all-mutual-friends []
  "Returns the complete map which has as keys a friend id and as vals the
   sequence of mutual friends. Memoized."
  (do
    (doseq [{id :id name :name} *friends*] (get-mutual-friends id))
    @*mutual-friends-map*))


(def *wall-count-map* (ref {}))
(defn get-wall-post-count
  "Returns a map which contains the friends-id and the number of post published
   on your wall. Memoized."
  [friend-id]
  (let [previous (get @*wall-count-map* friend-id)]
    (or previous
	(do (dosync
	     (alter *wall-count-map* assoc friend-id
		    (count (filter
			    (fn [{name :name id :id}] (= id friend-id))
			    (for [post *wall*] (get-in post [:from]))))))
	    (get @*wall-count-map* friend-id)))))


(defn get-all-wall-posters []
  "Returns the complete map which has as keys a friend id and as vals the
   number of simple post to your wall of mutual friend. Memoized."
  (do
    (doseq [{id :id name :name} *friends*] (get-wall-post-count id))
    @*wall-count-map*))


(def *commenters-map* (ref {}))
(defn get-comment-count
  "Returns a map which contains the friends-id and the number of comments left
   on your wall. Memoized"
  [friend-id]
  (let [previous (get @*commenters-map* friend-id)]
    (or previous
	(do (dosync
	     (alter *commenters-map* assoc friend-id
		    (count (filter
			    (fn [{name :name id :id}] (= id friend-id))
			    ;;Nested for (sorry :S ). The inner for search
			    ;;for comments, the outer returns a map of maps {:name :id}
			    (for [entry
				  (flatten (for [entry *wall*] (get-in entry [:comments :data])))]
			      (get-in entry [:from]))))))
	    (get @*commenters-map* friend-id)))))


(defn get-all-commenters []
  "Returns the complete map which has as keys a friend id and as vals the
   number of comments posted to your wall by that friend. Memoized."
  (do
    (doseq [{id :id name :name} *friends*] (get-comment-count id))
    @*commenters-map*))


(def *tags-count-map* (ref {}))
(defn get-photo-tags-count
  "Returns a map which contains the friends-id and the number of tags
   about you in your photos."
  [friend-id]
  (let [previous (get @*tags-count-map* friend-id)]
    (or previous
	(do (dosync
	     (alter *tags-count-map* assoc friend-id
		    (count (filter
			    (fn [{name :name id :id}] (= id friend-id))
			    (for [photo *photos-tags*] (get-in photo [:from]))))))
	    (get @*tags-count-map* friend-id)))))


(defn get-all-photo-taggers []
  "Returns the complete map which has as keys a friend id and as vals the
   number of photos which you are tagged in. Memoized."
  (do
    (doseq [{name :name id :id} *friends*] (get-photo-tags-count id))
    @*tags-count-map*))
  




