(ns newsface.templates.suggest
  (:use [hiccup core page-helpers]))


(defn suggest-page []
  (html5
    [:head
      [:title "Newsface - Suggesting you news based on your Facebook friends"]
      (include-css "/css/style.css")]
    [:body
     [:div.header
      [:a {:href "/"}
       [:img {:src "/imgs/logo.png"}]]]
     [:div.menu
      [:a {:href "/"} "Home"]
      [:a {:href "/accesstoken/"} "Access Token"]
      [:a {:href "/suggest/"} "Try it!"]
      [:a {:href "/contacts/"} "Contacts"]]
     [:div.content
      [:h2 "Try Newsface!"
       [:p
	"By clicking on the " [:b "Train"] " button below, "
	"Newsface will costruct your user profile." [:br]
	"The first time il will take several minutes in order "
	"to complete the process. Since Newsface is still in beta, we "
	"don't assure for the final result. The precision of the final "
	"result will depends from several factors:"
	[:ul
	 [:li "Number of resources on your Facebook profile"]
	 [:li "Number of resources from your strong tiers"]
	 [:li "Completeness of tags associated with the resources mentioned above."]]
	[:a {:href "/train/"}
	 [:img {:src "/imgs/train_btn.png"}]]]]]
     [:div.footer
      [:div.footer-wrap
       [:div.contact
	[:strong "Contact"] [:br]
	"Support: " [:a {:href "https://github.com/CharlesStain/newsface"}
		     "Project page"]
	[:br]
	"Feedback: " [:a {:href "mailto:alfredo.dinapoli@gmail.com"}
		      "alfredo.dinapoli@gmail.com"]]]]]))