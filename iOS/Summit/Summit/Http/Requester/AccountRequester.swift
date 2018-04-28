//
//  AccountRequester.swift
//  Summit
//
//  Created by Leapfrog-Software on 2018/04/29.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import Foundation

class AccountRequester {
    
    class func createUser(completion: @escaping ((Bool, String?) -> ())) {
        
        let params = [
            "command": "createUser"
        ]
        ApiManager.post(params: params) { (result, data) in
            if result, let userId = (data as? NSDictionary)?.object(forKey: "userId") as? String {
                completion(true, userId)
            } else {
                completion(false, nil)
            }
        }
    }
    
    class func updateUser(userData: UserData, completion: @escaping ((Bool) -> ())) {
        
        let params = [
            "command": "updateUser",
            "userId": userData.userId,
            "nameLast": userData.nameLast,
            "nameFirst": userData.nameFirst,
            "kanaLast": userData.kanaLast,
            "kanaFirst": userData.kanaFirst,
            "age": userData.age.toString(),
            "gender": userData.gender.toString(),
            "job": userData.job,
            "company": userData.company,
            "position": userData.position,
            "reserves": userData.reserves.joined(separator: "-"),
            "cards": userData.cards.joined(separator: "-")
        ]
        ApiManager.post(params: params) { (result, data) in
            completion(result)
        }
    }
    
    class func login(email: String, password: String, completion: @escaping ((Bool, String?) -> ())) {
        
        let params = [
            "command": "login",
            "email": email,
            "password": password
        ]
        ApiManager.post(params: params) { (result, data) in
            if result, let dic = (data as? Dictionary<String, String>), let userId = dic["userId"] {
                completion(true, userId)
            } else {
                completion(false, nil)
            }
        }
    }
}
