//
//  CardDetailViewController.swift
//  Summit
//
//  Created by Leapfrog-Software on 2018/04/30.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

class CardDetailViewController: UIViewController {
    
    @IBOutlet private weak var tableView: UITableView!

    private var userData: UserData!
    private var futureSchedules = [ScheduleData]()
    private var pastSchedules = [ScheduleData]()
    
    func set(userData: UserData) {
        self.userData = userData
        
        let today = Date()
        let reservedSchedules = userData.reserves.compactMap { ScheduleRequester.shared.query(id: $0) }
        self.futureSchedules = reservedSchedules.filter { $0.date >= today }
        self.pastSchedules = reservedSchedules.filter { $0.date < today }
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.tableView.rowHeight = UITableViewAutomaticDimension
        self.tableView.estimatedRowHeight = 200
    }
    
    @IBAction func onTapBack(_ sender: Any) {
        self.pop(animationType: .horizontal)
    }
}

extension CardDetailViewController: UITableViewDataSource, UITableViewDelegate {
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        var futureCount = self.futureSchedules.count
        if futureCount == 0 {
            futureCount = 1
        }
        var pastCount = self.pastSchedules.count
        if pastCount == 0 {
            pastCount = 1
        }
        return 3 + futureCount + pastCount
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        if indexPath.row == 0 {
            let cell = tableView.dequeueReusableCell(withIdentifier: "CardDetailUserInfoTableViewCell", for: indexPath) as! CardDetailUserInfoTableViewCell
            cell.configure(userData: self.userData)
            return cell
        } else if indexPath.row == 1 {
            let cell = tableView.dequeueReusableCell(withIdentifier: "CardDetailEventTitleTableViewCell", for: indexPath) as! CardDetailEventTitleTableViewCell
            cell.configure(title: "参加予定のイベント", count: self.futureSchedules.count)
            return cell
        } else if indexPath.row < 2 + self.futureSchedules.count {
            if self.futureSchedules.isEmpty {
                return tableView.dequeueReusableCell(withIdentifier: "CardDetailNoDataTableViewCell", for: indexPath)
            } else {
                let cell = tableView.dequeueReusableCell(withIdentifier: "CardDetailEventTableViewCell", for: indexPath) as! CardDetailEventTableViewCell
                cell.configure(scheduleData: self.futureSchedules[indexPath.row - 2])
                return cell
            }
        } else if indexPath.row == 2 + self.futureSchedules.count {
            let cell = tableView.dequeueReusableCell(withIdentifier: "CardDetailEventTitleTableViewCell", for: indexPath) as! CardDetailEventTitleTableViewCell
            cell.configure(title: "過去に参加したイベント", count: self.pastSchedules.count)
            return cell
        } else {
            if self.pastSchedules.isEmpty {
                return tableView.dequeueReusableCell(withIdentifier: "CardDetailNoDataTableViewCell", for: indexPath)
            } else {
                let cell = tableView.dequeueReusableCell(withIdentifier: "CardDetailEventTableViewCell", for: indexPath) as! CardDetailEventTableViewCell
                var futureCount = self.futureSchedules.count
                if futureCount == 0 {
                    futureCount = 1
                }
                cell.configure(scheduleData: self.pastSchedules[indexPath.row - 3 - futureCount])
                return cell
            }
        }
    }
}
