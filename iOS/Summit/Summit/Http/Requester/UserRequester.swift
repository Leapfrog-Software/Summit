//
//  UserRequester.swift
//  Summit
//
//  Created by Leapfrog-Software on 2018/04/29.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import Foundation

enum AgeType {
    case u20
    case s20
    case s30
    case s40
    case s50
    case o60
    case unknown
    
    static func create(value: String) -> AgeType {
        switch value {
        case "0":
            return .u20
        case "1":
            return .s20
        case "2":
            return .s30
        case "3":
            return .s40
        case "4":
            return .s50
        case "5":
            return .o60
        default:
            return .unknown
        }
    }
    
    func toText() -> String {
        switch self {
        case .u20:
            return "20歳未満"
        case .s20:
            return "20代"
        case .s30:
            return "30代"
        case .s40:
            return "40代"
        case .s50:
            return "50代"
        case .o60:
            return "60歳以上"
        case .unknown:
            return "(未設定)"
        }
    }
    
    func toString() -> String {
        switch self {
        case .u20:
            return "0"
        case .s20:
            return "1"
        case .s30:
            return "2"
        case .s40:
            return "3"
        case .s50:
            return "4"
        case .o60:
            return "5"
        default:
            return ""
        }
    }
    
    static func allValue() -> [AgeType] {
        return [.u20, .s20, .s30, .s40, .s50, .o60]
    }
}

enum GenderType {
    case male
    case female
    case unknown
    
    static func create(value: String) -> GenderType {
        switch value {
        case "0":
            return .male
        case "1":
            return .female
        default:
            return .unknown
        }
    }
    
    func toText() -> String {
        switch self {
        case .male:
            return "男性"
        case .female:
            return "女性"
        default:
            return "(未設定)"
        }
    }
    
    func toString() -> String {
        switch self {
        case .male:
            return "0"
        case .female:
            return "1"
        default:
            return ""
        }
    }
    
    static func allValue() -> [GenderType] {
        return [.male, .female]
    }
}

struct UserData {
    
    let userId: String
    var nameFirst: String
    var nameLast: String
    var kanaFirst: String
    var kanaLast: String
    var age: AgeType
    var gender: GenderType
    var job: String
    var company: String
    var position: String
    var reserves: [String]
    var cards: [String]
    var message: String
    
    init?(data: Dictionary<String, Any>) {
        
        guard let userId = data["id"] as? String else {
            return nil
        }
        self.userId = userId
        
        self.nameFirst = (data["nameFirst"] as? String)?.base64Decode() ?? ""
        self.nameLast = (data["nameLast"] as? String)?.base64Decode() ?? ""
        self.kanaFirst = (data["kanaFirst"] as? String)?.base64Decode() ?? ""
        self.kanaLast = (data["kanaLast"] as? String)?.base64Decode() ?? ""
        self.age = AgeType.create(value: (data["age"] as? String ?? ""))
        self.gender = GenderType.create(value: (data["gender"] as? String ?? ""))
        self.company = (data["company"] as? String)?.base64Decode() ?? ""
        self.job = (data["job"] as? String)?.base64Decode() ?? ""
        self.position = (data["position"] as? String)?.base64Decode() ?? ""
        self.reserves = data["reserves"] as? Array<String> ?? []
        self.cards = data["cards"] as? Array<String> ?? []
        self.message = (data["message"] as? String)?.base64Decode() ?? ""
    }
}

class UserRequester {
    
    static let shared = UserRequester()
    
    var dataList = [UserData]()
    
    func fetch(completion: @escaping ((Bool) -> ())) {
        
        let params = ["command": "getUser"]
        ApiManager.post(params: params) { [weak self] result, data in
            if result, let users = data as? Array<Dictionary<String, Any>> {
                self?.dataList = users.compactMap { UserData(data: $0) }
                completion(true)
            } else {
                completion(false)
            }
        }
    }
    
    func myUserData() -> UserData? {
        return self.dataList.filter { $0.userId == SaveData.shared.userId }.first
    }
    
    func query(userId: String) -> UserData? {
        return self.dataList.filter { $0.userId == userId }.first
    }
}
