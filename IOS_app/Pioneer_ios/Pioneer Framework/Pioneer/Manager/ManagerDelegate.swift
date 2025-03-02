//
//  ManagerDelegate.swift
//  Pioneer for IOS
//
//  Created by Beh on 2025/3/2.
//

import Foundation

public protocol ManagerDelegate {
    
}

public protocol RegisterDelegate: ManagerDelegate{
    func didRegister()
}

public protocol ContactLogManagerDelegate: ManagerDelegate{
    func matching(predicate: NSPredicate) -> [InsideEncounter]
    
}

public protocol TestManagerDelegate{
    
}

