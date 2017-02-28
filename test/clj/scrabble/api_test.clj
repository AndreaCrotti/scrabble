(ns scrabble.api-test
  (:require [scrabble.api :as api]
            [ring.mock.request :as mock]
            [clojure.data.json :as json :refer [read-str]]
            [clojure.test :as t]))


(t/deftest test-anagrams
  (t/testing "getting all the anagrams"
    (let [response (api/app (mock/request :get "/"))]
      (t/is (= (:status response) 200))
      (t/is (= (:body response) "Not defined"))))

  (t/testing "get top best words availle"
    (let [request (mock/request :get "/words" {:query-params {"one" ["complex" "type"]}})
          response (api/app request)]

      (t/is (= (:status response) 200))
      (t/is (= (-> response :body read-str) {})))))
