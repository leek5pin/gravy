(ns core-test
  (:require [core :refer :all]
            [clojure.test :refer :all]
            [clojure.spec.alpha :as s]
            [clojure.test.check :as tc]
            [orchestra.spec.test :as st]
            [clojure.test.check.properties :as prop :include-macros true]))

(st/instrument)

;; This test really isn't doing much, but neither is the endpoint we're testing, since it
;; only really has that one option, and the rest are more about the shape of the output.
;; However, it'll have to do, and at least it exercises our specs! 

(def guid-is-returned-prop
  (prop/for-all [guid (s/gen :game/guid)]
                (let [response (parse-response (get-game-memo guid))]
                  (or (= 0 (:number-of-total-results response))
                      (= guid (:guid (:results response)))))))

;; Tested with seed 1651442293128 locally size 200, I wouldn't be shocked if there were more
;; optional or nilable categories. That's what most the issues have been.
;;
;; We're going to keep size low so as to not blow up their endpoint.

(deftest guid-is-returned-test
  (tc/quick-check 10 #_200 guid-is-returned-prop #_#_:seed 1651442293128))
