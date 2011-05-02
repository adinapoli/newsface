;;Auth key: AI39si6x3yWFQjZkkPZT_gEbr8OzTzAmr4G94J_zhRmAfU9AHWxaY1Gxhq7nLmeSALcLZlpM0uBRBsM6nw3LmpbU8KMNA0sFOQ
(ns newsface.youtube
  (:use [newsface persistence] :reload)
  (:require [clojure.contrib.string :as str]
	    [clojure.xml :as xml]))


(defn get-youtube-entry
  "Return a seq (json like) representing the xml
   feed of the given youtube video."
  [video-id]
  (xml-seq (xml/parse (str "http://gdata.youtube.com/feeds/api/videos/" video-id))))


(defn get-title-from
  [parsed-xml]
  (try
    (let [search-query (fn [map-entry] (= (get map-entry :tag) :title))
	  result (first (filter search-query parsed-xml))]
      (map str/trim (str/split #" " (first (get result :content)))))
    (catch java.lang.NullPointerException e
      (println (str "Neither keywords nor title for: " parsed-xml)))))


(defn get-keywords-from
  "Given a parsed XML, search from a map like this:
  {:tag :media:keywords, :attrs nil, :content [keywords]} where keywords is a
  string that needs to be trimmed and splitted by ,
  This routine fallback on title splitting if no keywords are present."
  [parsed-xml]
  (try
    (let [search-query (fn [map-entry] (= (get map-entry :tag) :media:keywords))
	  result (first (filter search-query parsed-xml))]
      (map str/trim (str/split #"," (first (get result :content)))))
    (catch java.lang.NullPointerException e
      (get-title-from parsed-xml))))


(defn get-video-id
  "Given a valid youtube video link, returns the video id."
  [video-link]
  ;;Since I use a split by = or & the video-id will be between
  ;;the standard url (e.g. watch?v=) and the first get params (e.g &bla bla).
  (nth (str/split #"=|&" video-link) 1))


(defn is-a-youtube-link-p
  [url]
  (when-not (nil? url)
    (let [url-tokens (re-seq #"\w+" url)]
      (if (empty? (str/grep #"youtube" url-tokens)) false true))))


;;Da rivedere, bisogna gestire il caso dei video mancanti.
(defn get-videos-tags
  [user-id]
  (let [raw-data (get-links-urls-from user-id)
	urls (map (fn [{value :url}]  (when-not (nil? value) value)) raw-data)
	videos (filter is-a-youtube-link-p urls)]
    (for [link videos] {:id user-id :tags
			(get-keywords-from (get-youtube-entry (get-video-id link)))})))
