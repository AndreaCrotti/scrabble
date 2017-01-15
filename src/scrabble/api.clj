(ns scrabble.api
  (:gen-class)
  (:require [compojure
             [core :refer :all]
             [route :as route]]
            [clojure.data.json :as json]
            [scrabble.core :as scrabble]
            [ring.middleware.defaults :refer [api-defaults wrap-defaults]]))

(defn get-words [word]
  "Return all possible words"
  {:status 200
   :body (json/write-str (scrabble/possibilities word))}
  )


(defroutes app-routes
  (GET "/" [] "Not defined")
  (GET "/words" [word] (get-words word))
  (route/not-found "URL not found"))

(def app
  (wrap-defaults app-routes api-defaults))
