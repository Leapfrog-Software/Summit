//
//  AttendRequester.swift
//  Summit
//
//  Created by Leapfrog-Software on 2018/04/29.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import Foundation

struct AttendData {
    
    let tableId: String
    var userIds: [String]
    
    init?(data: Dictionary<String, Any>) {
        
        guard let tableId = data["tableId"] as? String else {
            return nil
        }
        self.tableId = tableId
        self.userIds = data["userIds"] as? Array<String> ?? []
    }
}

struct ChatData {
    
    let id: String
    let senderId: String
    let tableId: String
    let datetime: String
    let chat: String
    
    init?(data: Dictionary<String, Any>) {
        
        guard let id = data["id"] as? String else {
            return nil
        }
        self.id = id
        self.senderId = data["senderId"] as? String ?? ""
        self.tableId = data["tableId"] as? String ?? ""
        self.datetime = data["datetime"] as? String ?? ""
        self.chat = data["chat"] as? String ?? ""
    }
}

class AttendRequester {
    
    var scheduleId = ""
    var chatList = [ChatData]()
    var attendList = [AttendData]()
    
    func initialize(scheduleId: String) {
        self.scheduleId = scheduleId
    }
    
    func attend(tableId: String, completion: @escaping ((Bool) -> ())) {
        
        let params = [
            "command": "attend",
            "scheduleId": self.scheduleId,
            "userId": SaveData.shared.userId,
            "tableId": tableId
        ]
        ApiManager.post(params: params) { [weak self] result, data in
            if result, let datas = data as? Dictionary<String, Any> {
                if let chats = datas["chats"] as? Array<Dictionary<String, Any>>,
                    let attends = datas["attends"] as? Array<Dictionary<String, Any>> {
                    self?.chatList = chats.compactMap { ChatData(data: $0) }
                    self?.attendList = attends.compactMap { AttendData(data: $0) }
                    completion(true)
                    return
                }
            }
            completion(false)
        }
    }
    
    func postChat(chatId: String, tableId: String, chat: String, completion: @escaping ((Bool) -> ())) {
        
        let params = [
            "command": "postChat",
            "scheduleId": self.scheduleId,
            "chatId": chatId,
            "senderId": SaveData.shared.userId,
            "tableId": tableId,
            "chat": chat
        ]
        ApiManager.post(params: params) { result, _ in
            completion(result)
        }
    }
}
