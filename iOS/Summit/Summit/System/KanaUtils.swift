//
//  KanaUtils.swift
//  Summit
//
//  Created by Leapfrog-Software on 2018/04/30.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import Foundation

class KanaUtils {
    
    class var kanas: [[String]] {
        return [
            ["あ", "い", "う", "え", "お"],
            ["か", "き", "く", "け", "こ", "が", "ぎ", "ぐ", "げ", "ご"],
            ["さ", "し", "す", "せ", "そ", "ざ", "じ", "ず", "ぜ", "ぞ"],
            ["た", "ち", "つ", "っ", "て", "と", "だ", "ぢ", "づ", "で", "ど"],
            ["な", "に", "ぬ", "ね", "の"],
            ["は", "ひ", "ふ", "へ", "ほ", "ば", "び", "ぶ", "べ", "ぼ", "ぱ", "ぴ", "ぷ", "ぺ", "ぽ"],
            ["ま", "み", "む", "め", "も"],
            ["や", "ゃ", "ゆ", "ゅ", "よ", "ょ"],
            ["ら", "り", "る", "れ", "ろ"],
            ["わ", "を", "ん"],
        ]
    }
    
    class func columnIndex(of: String) -> Int? {
        
        return kanas.index(where: { kana -> Bool in
            return kana.contains(of)
        })
    }
    
    private class func compare(c1: String, c2: String) -> Int {
        
        guard let column1 = columnIndex(of: c1) else {
            return -1
        }
        guard let column2 = columnIndex(of: c2) else {
            return 1
        }
        
        if column1 != column2 {
            return (column1 < column2) ? 1 : -1
        } else {
            guard let i1 = kanas[column1].index(of: c1) else {
                return -1
            }
            guard let i2 = kanas[column2].index(of: c2) else {
                return 1
            }
            if i1 == i2 {
                return 0
            } else {
                return (i1 < i2) ? 1 : -1
            }
        }
    }
    
    class func compare(s1: String, s2: String) -> Bool {
        
        for i in 0..<s1.count {
            if s2.count <= i {
                return false
            }
            let c1 = String(s1[s1.index(s1.startIndex, offsetBy: i)..<s1.index(s1.startIndex, offsetBy: i + 1)])
            let c2 = String(s2[s2.index(s2.startIndex, offsetBy: i)..<s2.index(s2.startIndex, offsetBy: i + 1)])
            let res = KanaUtils.compare(c1: c1, c2: c2)
            if res == 1 {
                return true
            } else if res == -1 {
                return false
            }
        }
        return true
    }
}
