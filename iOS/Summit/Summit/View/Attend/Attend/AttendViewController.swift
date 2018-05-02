//
//  AttendViewController.swift
//  Summit
//
//  Created by Leapfrog-Software on 2018/05/01.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

class AttendViewController: UIViewController {
    
    struct Const {
        static let timerInterval = Double(5)
    }

    @IBOutlet private weak var closeButton: UIButton!
    @IBOutlet private weak var scrollView: UIScrollView!
    @IBOutlet private weak var scrollViewLeadingConstraint: NSLayoutConstraint!
    
    private var scheduleData: ScheduleData!
    private var members = [UserData]()
    private var currentTableId: String!
    private var attendRequester: AttendRequester!
    private var chatViewController: AttendChatViewController!
    
    func set(scheduleData: ScheduleData, initialTableId: String) {
        self.scheduleData = scheduleData
        self.currentTableId = initialTableId
        
        self.members = UserRequester.shared.dataList.filter { $0.reserves.contains(self.scheduleData.id) }
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.attendRequester = AttendRequester()
        self.attendRequester.initialize(scheduleId: self.scheduleData.id)

        self.initTables()
        self.initChatView()
        
        (self.view as? AttendHitView)?.delegate = self
        
        self.timerProc()
        Timer.scheduledTimer(withTimeInterval: Const.timerInterval, repeats: true, block: { _ in
            self.timerProc()
        })
    }
    
    private func initTables() {

        var tableCount = 1
        if self.members.count % 2 == 0 {
            tableCount = self.members.count / 2
        } else if self.members.count >= 3 {
            tableCount = self.members.count / 2 + 1
        }
        
        let cellNum = Int(sqrt(Double(tableCount))) + 1
        let tableSize = CGSize(width: UIScreen.main.bounds.size.width - 2 * self.scrollViewLeadingConstraint.constant,
                               height: UIScreen.main.bounds.size.width - 2 * self.scrollViewLeadingConstraint.constant)
        
        for i in 0..<tableCount {
            let attendTableView = UINib(nibName: "AttendTableView", bundle: nil).instantiate(withOwner: nil, options: nil).first as! AttendTableView
            attendTableView.tag = i
            let x = i % cellNum
            let y = i / cellNum
            
            attendTableView.frame = CGRect(x: CGFloat(x) * tableSize.width,
                                           y: CGFloat(y) * tableSize.height,
                                           width: tableSize.width,
                                           height: tableSize.height)
            self.scrollView.addSubview(attendTableView)
            
            if i == tableCount - 1 {
                self.scrollView.contentSize = CGSize(width: CGFloat(cellNum) * tableSize.width,
                                                     height: CGFloat(y + 1) * tableSize.height)
            }
        }
    }
    
    private func initChatView() {

        let chatViewController = self.viewController(storyboard: "Attend", identifier: "AttendChatViewController") as! AttendChatViewController
        self.stack(viewController: chatViewController, animationType: .none)
        chatViewController.view.frame = CGRect(x: 0,
                                               y: UIScreen.main.bounds.size.height - AttendChatViewController.Const.chatBottomLimit,
                                               width: self.view.frame.size.width,
                                               height: self.view.frame.size.height - AttendChatViewController.Const.chatTopLimit)
        chatViewController.delegate = self
        self.chatViewController = chatViewController
    }
    
    private func timerProc() {
        
        self.attendRequester.attend(tableId: self.currentTableId, completion: { result in
            if !result {
                return
            }
            self.attendRequester.attendList.forEach { attendData in
                guard let tableId = Int(attendData.tableId),
                    let tableView = ((self.scrollView.subviews.compactMap { $0 as? AttendTableView }).filter { $0.tag == tableId }).first else {
                    return
                }
                tableView.set(userIds: attendData.userIds)
            }
            let chatList = self.attendRequester.chatList.filter { $0.tableId == self.currentTableId }
            self.chatViewController.set(tableId: self.currentTableId, chatList: chatList)
        })
    }
    
    @IBAction func onTapClose(_ sender: Any) {
        self.pop(animationType: .vertical)
    }
}

extension AttendViewController: AttendViewDelegate {
    
    func hitTest(_ point: CGPoint, with event: UIEvent?) -> UIView? {
        
        if self.closeButton.absoluteFrame().contains(point) {
            return nil
        }
        if point.y > self.chatViewController.view.frame.origin.y {
            return nil
        }
        return self.scrollView
    }
}

extension AttendViewController: AttendChatDelegate {
    
    func didTapSend(chatId: String, chat: String) {
        
        self.attendRequester.postChat(chatId: chatId, tableId: self.currentTableId, chat: chat, completion: { result in
            
        })
    }
}
