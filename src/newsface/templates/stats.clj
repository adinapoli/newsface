(ns newsface.templates.stats
  (:use
   [newsface persistence ranking reports]
   [hiccup core page-helpers] :reload))

(def *imgs*
     ["topten.png" "topmutuals.png" "topposts.png" "topphotos.png"
      "topcomments.png" "toplikes.png"])

(def *graphics*
     (map str (repeat (count *imgs*) "/imgs/") *imgs*))

(defn stats-page []
  (do
    (println (strong-tiers))
    (save-graph-to (get-graph (strong-tiers) "Overall Top 10")
	       "topten.png" "resources/public/imgs/" 1000)
    (save-graph-to (get-graph @*mutual-friends* "Top 10 by Mutual Friends")
	       "topmutuals.png" "resources/public/imgs/" 1000)
    (save-graph-to (get-graph @*wall-posters* "Top 10 by Wall Posts")
	       "topposts.png" "resources/public/imgs/" 1000)
    (save-graph-to (get-graph @*photo-taggers* "Top 10 by Photos Tags")
	       "topphotos.png" "resources/public/imgs/" 1000)
    (save-graph-to (get-graph @*commenters* "Top 10 by Comments")
	       "topcomments.png" "resources/public/imgs/" 1000)
    (save-graph-to (get-graph @*likers* "Top 10 by Likes")
		   "toplikes.png" "resources/public/imgs/" 1000)

    (html5
     [:head
      [:title "Newsface - Suggesting you news based on your Facebook friends"]
      (include-css "/css/style.css")
      (include-js "/js/search.js")]
     [:body
      [:div.header
       [:a {:href "/"}
	[:img {:src "/imgs/logo.png"}]]]
      [:div.menu
       [:a {:href "/"} "Home"]
       [:a {:href "/accesstoken/"} "Access Token"]
       [:a {:href "/suggest/"} "Try it!"]
       [:a {:href "/news/"} "My News"]
       [:a {:href "/contacts/"} "Contacts"]]
      [:div.content
       [:h2 "Graphic Summary"
	[:p "This is what Newsface have inferred analizing your profile:" [:br]
	 (for [path *graphics*]
	   [:img {:src path}])]]]
      [:div.footer
       [:div.footer-wrap
	[:div.contact
	 [:strong "Contact"] [:br]
	 "Support: " [:a {:href "https://github.com/CharlesStain/newsface"}
		      "Project page"]
	 [:br]
	 "Feedback: " [:a {:href "mailto:alfredo.dinapoli@gmail.com"}
		       "alfredo.dinapoli@gmail.com"]]]]])))