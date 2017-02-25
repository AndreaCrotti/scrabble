(ns scrabble.events
  (:require [re-frame.core :as re-frame :refer [reg-event-db]]
            [scrabble.db :as db]
            [goog.string :as gstring]
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

(reg-event-db
 :set-tile
 (fn [db [_ idx tile]]
   (assoc-in db [:tiles idx] tile)))
