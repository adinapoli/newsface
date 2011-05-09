(ns newsface.gdata
  (:require
   [net.cgrand.enlive-html :as en]))


;;Not needed
(def *auth-key* "ABQIAAAA26vPnnJWBcCG6cgCQi8iFxTJQa0g3IQ9GZqIMmInSLzwtGDKaBSeNhheb8B4HlYnHp4u2bnwU1jScQ")


(def *query-url* "http://news.google.it/news/search?q=")


(defn take-if-valid
  "Given a sequence representing a title, checks if title is valid.
  A title, in order to be valid, must be composed at least by two chuncks,
  e.g the search keywords and another string."
  [title-seq]
  (when (> (count title-seq) 1)
    title-seq))


(defn prettify-title
  [title-seq]
  (apply str (flatten (map #(or (:content %1) %1)
			   title-seq))))


(defn news-search
  "Given a bunch of keywords, extract the first 10 news."
  [keywords]
  (let [url (str *query-url* (apply str (interpose "+" keywords)))
	parsed-page (en/html-resource (java.net.URL. url))
	news (-> (en/select parsed-page [[:a (en/attr-contains :class "article")]]))]
    (filter (fn [{href :href title :title}] (not= "" title))
	    (for [elem news]
	      {:href  (-> elem :attrs :href)
	       :title (-> elem :content first :content take-if-valid prettify-title)}))))