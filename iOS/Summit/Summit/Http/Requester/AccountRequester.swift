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
            "nameLast": userData.nameLast.base64Encode() ?? "",
            "nameFirst": userData.nameFirst.base64Encode() ?? "",
            "kanaLast": userData.kanaLast.base64Encode() ?? "",
            "kanaFirst": userData.kanaFirst.base64Encode() ?? "",
            "age": userData.age.toString(),
            "gender": userData.gender.toString(),
            "job": userData.job.base64Encode() ?? "",
            "company": userData.company.base64Encode() ?? "",
            "position": userData.position.base64Encode() ?? "",
            "reserves": userData.reserves.joined(separator: "-"),
            "cards": userData.cards.joined(separator: "-"),
            "message": userData.message.base64Encode() ?? ""
        ]
        ApiManager.post(params: params) { (result, data) in
            completion(result)
        }
    }    
}
