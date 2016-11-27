(ns vr-playground.prod
  (:require [vr-playground.core :as core]))

;;ignore println statements in prod
(set! *print-fn* (fn [& _]))

(core/init!)
