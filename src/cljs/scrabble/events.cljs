(ns scrabble.events
  (:require [re-frame.core :as re-frame :refer [reg-event-db]]
            [scrabble.db :as db]
            [scrabble.query :as query :refer [find-matches]]
            [ajax.core :refer [GET POST]]))

(reg-event-db
 :initialize-db
 (fn  [_ _]
   db/default-db))

(defn word-handler [response]
  (prn response))

(defn word-error-handler [error]
  (prn error))

(reg-event-db
 :get-meaning
 ;; get the actual word to ask here
 (fn [db [_ word]]
   ;; find an actual API that can do this job properly
   (GET "http://www.dictionary.com/browse/api"
        {:handler word-handler
         :error-handler word-error-handler}
    )))

(reg-event-db
 :set-language
 (fn [db [_ language]]
   (assoc db :language language)))

;; the number of points will depend automatically on the language that was currently chosen

;; these two are really the same thing in a way
(reg-event-db
 :set-tile
 (fn [db [_ idx tile]]
   (assoc-in db [:tiles idx] tile)))

(reg-event-db
 :set-letter
 (fn [db [_ idx tile]]
   (assoc-in db [:letters idx] tile)))

(reg-event-db
 :get-results
 (fn [db _]
   ;; use juxt here instead?
   (assoc db :results (find-matches (:language db) (:tiles db) (:letters db)))))
