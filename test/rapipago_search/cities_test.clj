(ns rapipago-search.cities-test
  (:require [clojure.test :refer :all]
            [vcr-clj.clj-http :refer [with-cassette]]
            [rapipago-search.cities :as cities]))

(deftest find-in-province
  (with-cassette :cities-in-salta
    (def salta {:id "A" :name "Salta"})
    (def salta-cities (cities/find-in-province salta))
    (def names (map :name salta-cities))
    (is (> (count salta-cities) 5))
    (is (every? (set names) ["CAFAYATE" "EMBARCACION"]))))

