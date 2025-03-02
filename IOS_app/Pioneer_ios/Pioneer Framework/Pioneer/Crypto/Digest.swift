//
//  Digest.swift
//  Pioneer for IOS
//
//  Created by Beh on 2025/3/2.
//

import Foundation

public protocol Digest
{
    func getAlgorithmName() -> String
    func getDigestSize() -> Int
    func update(inbyte:UInt8)
    func update(inbytes:Array<UInt8>,inOff:Int,inLen:Int)
    func doFinal(outbytes:inout Array<UInt8>,outOff:Int) -> Int
    func reset()
}
