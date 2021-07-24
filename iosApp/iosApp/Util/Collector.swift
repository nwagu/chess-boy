//
//  Collector.swift
//  iosApp
//
//  Created by Chukwuemeka Nwagu on 20/07/2021.
//  Copyright © 2021 Chukwuemeka Nwagu. All rights reserved.
//

import sharedmodels

class Collector<T>: FlowCollector, ObservableObject {
    
    @Published var currentValue: T
    
    init(initialValue: T) {
        self.currentValue = initialValue
    }

    func emit(value: Any?, completionHandler: @escaping (KotlinUnit?, Error?) -> Void) {
        currentValue = (value as! T)
        completionHandler(KotlinUnit(), nil)
    }
}

extension Flow {
    func collectAsObservable<T>(initialValue: T) -> Collector<T> {
        
        let observableOutput: Collector<T> = Collector<T>(initialValue: initialValue)
        collect(collector: observableOutput) { (unit, error) in }
        
        return observableOutput
        
    }
}
