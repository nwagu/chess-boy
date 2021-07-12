//
//  Stack.swift
//  iosApp
//
//  Created by Chukwuemeka Nwagu on 08/07/2021.
//  Copyright © 2021 orgName. All rights reserved.
//

struct Stack<Element> {
    
    var items = [Element]()
    
    mutating func push(_ item: Element) {
        items.append(item)
    }
    mutating func pop() -> Element? {
        return items.popLast()
    }
    func peek() -> Element? {
        guard let top = items.last else { return nil }
        return top
    }
}
