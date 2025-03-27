//
//  ExtendedDigest.swift
//  Pioneer for IOS
//
//  Created by Beh on 2025/3/2.
//

import Foundation

public protocol ExtendedDigest : Digest
{
    func getByteLength() -> Int
}
