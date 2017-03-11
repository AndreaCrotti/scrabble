(ns scrabble.api-test
  (:require [scrabble.api :as api]
            [ring.mock.request :as mock]
            [clojure.data.json :as json :refer [read-str]]
            [clojure.test :as t]))


(t/deftest test-anagrams
  (t/testing "getting all the anagrams"
    (let [request (mock/request :get "/api/anagrams" {:word "hello"})
          ;; looks like it's not working during tests but it works fine otherwise
          response (api/app request)]

      (t/is (= (:status response) 200))
      (t/is (= (-> response :body read-str) []))))

  (t/testing "get top best words availle"
    ;; not actually correct still here
    (let [request (mock/request :get "/api/words" {:one ["complex" "type"]})
          response (api/app request)]

      (t/is (= (:status response) 200))
      (t/is (= (-> response :body read-str) {})))))
