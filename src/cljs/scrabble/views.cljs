(ns scrabble.views
  (:require [re-frame.core :as re-frame :refer [dispatch subscribe]]
            [scrabble.constants :as const]
            [goog.string :as gstring]
            [cljs.spec.alpha :as spec]))

;; the page is composed of
;; - a tile editor: to be able to enter the current situation
;; - the available letters
;;
;; Once the tile and the letters are given, we can provide the various
;; options in graphical form, sorting by the most convenient first

(defn- set-lang [lang]
  (dispatch [:set-language lang]))

(def flag-files
  (zipmap const/AVAILABLE-LANGUAGES
          (map #(subs (str % ".png") 1) const/AVAILABLE-LANGUAGES)))

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

(defn lang-selection
  "Define all the possible languages as sequences of clickable images"
  []
  (let [current-language (subscribe [:current-language])]
    (fn []
      (into
       [:div {:class "language-group"}]
       (for [lang const/AVAILABLE-LANGUAGES]
         (make-lang lang @current-language))))))

(defn make-tile [idx]
  (let [tile (subscribe [:tile idx])]
    (fn [idx]
      ;; very simple input box with 1 or two chars
      ;; valid values could be 1/2p/3p/2l/3l
      ;; TODO: show some validation directly in here
      [:input {:type "text"
               :placeholder @tile
               :class "tile-input"
               :on-change #(dispatch [:set-tile idx (-> % .-target .-value)])}])))

(defn available-letters []
  (fn []
    (into
     [:div {:class "letters-editor"}]
     (for [n (range const/MAX-LETTERS)]
       (let [letter (subscribe [:letter n])]
         [:input {:type "text"
                  ;;:value @letter
                  :class "letter-input"
                  :on-change #(dispatch [:set-letter n (-> % .-target .-value)])}])))))

(defn results []
  (let [results (subscribe [:results])]
    (fn []
      (into
       [:table {:class "best-word-table"}]
       (map (fn [[fst snd]] [:tr [:td fst] [:td snd]]) @results)))))

(defn anagram-input []
  (let [inp (subscribe [:word-to-anagram])]
    (fn []
      [:div {:class "anagram-input"}
       [:input {:type "text"
                :value @inp
                :on-change #(dispatch [:set-word-to-anagram (-> % .-target .-value)])}]])))

(defn anagrams-results
  "Show the anagrams in a table"
  []
  (let [ans (subscribe [:anagrams])]
    (fn []
      (into
       [:table {:class "ans"}]
       (map (fn [an] [:tr [:td (first an)] [:td (second an)]]) @ans)))))

(defn main-panel []
  ;; might even not need a function at all here
  (fn []
    ;; make it possible to change the number of players??
    [:g
     [lang-selection]

     (into [:g {:class "tile-editor"}
            (for [n (range const/MAX-TILES)]
              [make-tile n])])

     [available-letters]

     ;; how can I add more classes?
     [:button {:id "submit-button"
               :on-click #(dispatch [:get-results])}
      "Give me the best words!"]

     [:button {:id "clear best word search"
               :on-click #(dispatch [:clear-best-word])}
      "Clear best word search"]

     ;; legend reminding all the available values
     [results]
     [anagram-input]

     [:button {:id "get-anagrams"
               :on-click #(dispatch [:fetch-anagrams])}
      "Show the anagrams"]

     [:button {:id "clear anagrams"
               :class "clear"
               :on-click #(dispatch [:clear-anagrams])}
      "Clear anagrams search"]

     #_(fn [] (let [wd (subscribe [:word-to-anagram])]
             (when wd)
             [:div (str "Anagrams for " wd " :")]))

     [anagrams-results]]))
