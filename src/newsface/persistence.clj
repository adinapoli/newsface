(ns newsface.persistence
  (:use
   [somnium congomongo]))


;; I wanna try to use a NoSQL DB, and since Facebook as well as MongoDB
;; speaks the JSON language, this is the perfect environment to work in.

;; Database structure
;; :friends {:name :id}
;; :wall
;; :photos-tags
;; :mutual {:id :count}
;; :posters {:id :count}
;; :commenters {:id :count}

(mongo! :db "newsface")


(defn insert
  "Store a resource into a table, given the :table-name and
  the resource itself as a key-value pair."
  [table-name object]
  (insert! table-name object))


(defn mass-insert
  "Store a sequence of objects inside a table."
  [table-name obj-seq]
  (mass-insert! table-name obj-seq))