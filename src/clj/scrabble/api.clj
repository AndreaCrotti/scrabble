(ns scrabble.api
  (:gen-class)
  (:require [compojure
             [core :refer [GET POST defroutes]]
             [route :as route]]
            [clojure.data.json :as json]
            [compojure.handler :refer [site]]
            [scrabble.core :as scrabble]
            [ring.adapter.jetty :as jetty]
            [ring.middleware.json :refer [wrap-json-response]]
            [ring.middleware.defaults :refer [api-defaults wrap-defaults]]))

(defn valued-anagrams
  "Return all the anagrams in reverse order of value"
  [word]
  (let [ans (scrabble/anagrams word)]
    (sort-by second
             (zipmap ans (map scrabble/word-value ans)))))

(defn get-words
  "Return all possible words"
  [word]
  {:status 200
   :body (json/write-str (valued-anagrams word))
   ;; TODO: implement some more proper security here instead
   ;; with the right settings on deployment
   :headers {"Access-Control-Allow-Origin" "*"
             "Access-Control-Allow-Headers" "Content-Type"
             ;; should this not be done already by the wrap-json middleware?
             "Content-Type" "application/json"}})

(defn best-words [tiles word]
  (let [res (scrabble/best-words tiles word)]
    {:status 200
     :body (json/write-str res)}))

(defroutes app-routes
  (GET "/" [] "Not defined")
  (GET "/anagrams" [word] (get-words word))
  (GET "/best-words" [tiles word] (best-words tiles word))
  (GET "/words" [one] {:status 200 :body "{}"})
  (route/not-found "URL not found"))

(def app
  (wrap-json-response app-routes api-defaults))

(defn -main [& [port]]
  (let [port (Integer. (or port 5000))]
    (jetty/run-jetty (site #'app) {:port port :join? false})))
