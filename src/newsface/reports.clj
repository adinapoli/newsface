(ns newsface.reports
  (:use
   [newsface core persistence ranking]
   [incanter core charts stats] :reload))


(defn get-graph
  "Return an Incanter's graph object."
  [seq title]
  (let [domain (take 10 seq)
        x-axes (for [{id :id cnt :count} domain] (get-friend-name id))
        y-axes (for [{id :id cnt :count} domain] cnt)]
    (bar-chart x-axes y-axes :title title :x-label "Friends")))


(defn save-graph-to
  [graph filename dir width]
  (save graph (str dir filename) :width width))


(defn view-metric
  "Display a bar chart with the top ten results according to the given metric."
  [seq title]
  (view  (get-graph seq title)))


(defn view-top-ten-mutuals
  "Show an histogram representing the top ten friends according to the
   number of mutual friends."
  []
  (view-metric @*mutual-friends* "Top 10 for Mutual Friends"))


(defn view-top-ten-wall-posters
  "Show an histogram representing the top ten friends according to the
   number of simple post on your wall."
  []
  (view-metric @*wall-posters* "Top 10 Wall Posters"))


(defn view-top-ten-photo-taggers
  "Show an histogram representing the top ten friends according to the
   number of tags in your photos."
  []
  (view-metric @*photo-taggers* "Top 10 Photo Taggers"))


(defn view-top-ten-commenters
  "Show an histogram representing the top ten friends according to the
   comment posted on your wall."
  []
  (view-metric @*commenters* "Top 10 Commenters"))


(defn view-top-ten-likers
  "Show an histogram representing the top ten friends according to
   the number of likes on your posts."
  []
  (view-metric @*likers* "Top 10 Likers"))


(defn view-top-ten
  "Show an histogram representing the top ten strong tiers."
  []
  (view-metric (strong-tiers) "Top 10 Strong Tiers"))