(ns newsface.reports
  (:use
   [newsface core]
   [incanter core charts stats] :reload))


(defn view-top-ten-mutuals
  "Show an histogram representing the top ten friends according to the
   number of mutual friends."
  []
  (let [mutuals *all-mutual-friends*
        id2count (for [id (keys mutuals)] {:id id :count (count (get mutuals id))})
        result (sort-by (fn [{id :id cnt :count}] cnt) id2count) ;;ascending order
        domain (reverse (take-last 10 result)) ;;need to order in discending
        x-axes (for [{id :id cnt :count} domain] (get-friend-name id))
        y-axes (for [{id :id cnt :count} domain] cnt)]
    (view (set-title (bar-chart x-axes y-axes) "Top 10 for Mutual Friends"))))


(defn view-top-ten-wall-posters
  "Show an histogram representing the top ten friends according to the
   number of simple post on your wall."
  []
  (let [all-posters *all-wall-posters*
        id2posts (for [id (keys all-posters)] {:id id :count (get all-posters id)})
	      result (sort-by (fn [{id :id cnt :count}] cnt) id2posts) ;;ascending order
	      domain (reverse (take-last 10 result)) ;;need to order in discending
	      x-axes (for [{id :id cnt :count} domain] (get-friend-name id))
	      y-axes (for [{id :id cnt :count} domain] cnt)]
    (view (set-title (bar-chart x-axes y-axes) "Top 10 Wall Posters"))))


(defn view-top-ten-photo-taggers
  "Show an histogram representing the top ten friends according to the
   number of tags in your photos."
  []
  (let [all-taggers *all-photo-taggers*
        id2tags (for [id (keys all-taggers)] {:id id :count (get all-taggers id)})
	      result (sort-by (fn [{id :id cnt :count}] cnt) id2tags) ;;ascending order
	      domain (reverse (take-last 10 result)) ;;need to order in discending
	      x-axes (for [{id :id cnt :count} domain] (get-friend-name id))
	      y-axes (for [{id :id cnt :count} domain] cnt)]
    (view (set-title (bar-chart x-axes y-axes) "Top 10 Photo Taggers"))))


(defn view-top-ten-commenters
  "Show an histogram representing the top ten friends according to the
   comment posted on your wall."
  []
  (let [all-commenters *all-commenters*
        id2com (for [id (keys all-commenters)] {:id id :count (get all-commenters id)})
	      result (sort-by (fn [{id :id cnt :count}] cnt) id2com) ;;ascending order
	      domain (reverse (take-last 10 result)) ;;need to order in discending
	      x-axes (for [{id :id cnt :count} domain] (get-friend-name id))
	      y-axes (for [{id :id cnt :count} domain] cnt)]
    (view (set-title (bar-chart x-axes y-axes) "Top 10 Commenters"))))
  