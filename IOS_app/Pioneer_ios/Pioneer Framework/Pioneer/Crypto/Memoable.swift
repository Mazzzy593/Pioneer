//
//  Memoable.swift
//  Pioneer for IOS
//
//  Created by Beh on 2025/3/2.
//

import Foundation

public protocol Memoable
{
    func copy() -> Memoable
    
    func reset(from other:Memoable)
}
