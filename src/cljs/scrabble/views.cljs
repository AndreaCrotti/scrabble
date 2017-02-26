(ns scrabble.views
  (:require [re-frame.core :as re-frame :refer [dispatch subscribe]]
            [goog.string :as gstring]
            [cljs.spec :as spec]))

(def ^:const MAX-TILES 10)
(def ^:const MAX-LETTERS 7)
(def ^:const AVAILABLE-LANGUAGES ^:const #{:english :italian})

;; the page is composed of
;; - a tile editor: to be able to enter the current situation
;; - the available letters
;;
;; Once the tile and the letters are given, we can provide the various
;; options in graphical form, sorting by the most convenient first

(defn- set-lang [lang]
  (dispatch [:set-language lang]))

(def flag-files
  {:italian "italian.png"
   :english "english.png"})

(defn make-lang [lang current-language]
  (let [selected (= lang current-language)
        png-file (lang flag-files)
        props {:type "image" :src png-file}
        full-props (if selected
                     (assoc props :class "language.selected" )
                     (assoc props
                            :class "language.not-selected"
                            :on-click #(dispatch [:set-language lang])))]

    [:input full-props]))

;; this should be in a header on the right, find out how to align it properly

(defn lang-selection []
  (let [current-language (subscribe [:current-language])]
    (into
     [:div {:class "language-group"}]
     (for [lang AVAILABLE-LANGUAGES]
       (make-lang lang @current-language)))))

(defn make-tile [idx]
  (let [tile (subscribe [:tile idx])]
    ;; very simple input box with 1 or two chars
    ;; valid values could be 1/2p/3p/2l/3l
    ;; TODO: show some validation directly in here
    [:input {:type "text"
             :placeholder @tile
             :class "tile-input"
             :on-change #(dispatch [:set-tile idx (-> % .-target .-value)])}]))

(defn available-letters []
  (into
   [:div {:class "letters-editor"}]
   (for [n (range MAX-LETTERS)]
     (let [letter (subscribe [:letter n])]
       [:input {:type "text"
                :value @letter
                :class "letter-input"
                :on-change #(dispatch [:set-letter n (-> % .-target .-value)])}]))))

(defn main-panel []
  ;; might even not need a function at all here
  (fn []
    ;; make it possible to change the number of players??
    (into [:g]
          (concat
           [(lang-selection)]
           [(into
             [:g {:class "tile-editor"}]

             (for [n (range MAX-TILES)]
               (make-tile n)))]

           [(available-letters)]))))
