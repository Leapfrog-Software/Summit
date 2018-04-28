//
//  UserImageUploader.swift
//  Summit
//
//  Created by Leapfrog-Software on 2018/04/29.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

class UserImageUploader {
    
    class func post(image: UIImage, completion: @escaping ((Bool) -> ())) {
        
        let params = [
            "command": "uploadUserImage",
            "userId": SaveData.shared.userId
        ]
        ImageUploader.post(url: Constants.ServerApiUrl, image: image, params: params, completion: { result, data in
            if result, let data = data {
                do {
                    if let json = try JSONSerialization.jsonObject(with: data, options: JSONSerialization.ReadingOptions.allowFragments) as? Dictionary<String, Any> {
                        if let result = json["result"] as? String, result == "0" {
                            completion(true)
                            return
                        }
                    }
                } catch {}
            }
            completion(false)
        })
    }
}
