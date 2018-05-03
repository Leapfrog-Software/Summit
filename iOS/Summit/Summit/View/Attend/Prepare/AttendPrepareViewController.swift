//
//  AttendPrepareViewController.swift
//  Summit
//
//  Created by Leapfrog-Software on 2018/05/01.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

class AttendPrepareViewController: UIViewController {
    
    @IBOutlet private weak var scheduleImageView: UIImageView!
    @IBOutlet private weak var nameLabel: UILabel!
    @IBOutlet private weak var remainTimeTitleLabel: UILabel!
    @IBOutlet private weak var remainTimeLabel: UILabel!
    @IBOutlet private weak var dateLabel: UILabel!
    @IBOutlet private weak var memberCountLabel: UILabel!
    @IBOutlet private weak var providerLabel: UILabel!
    @IBOutlet private weak var descriptionLabel: UILabel!

    private var scheduleData: ScheduleData!
    private var members = [UserData]()
    
    func set(scheduleData: ScheduleData) {
        self.scheduleData = scheduleData
        
        self.members = UserRequester.shared.dataList.filter { userData -> Bool in
            return userData.reserves.contains(self.scheduleData.id)
        }
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        ImageStorage.shared.fetch(url: Constants.ScheduleImageDirectory + self.scheduleData.id, imageView: self.scheduleImageView)
        
        self.nameLabel.text = "【" + self.scheduleData.title + "】"
        self.dateLabel.text = DateFormatter(dateFormat: "yyyy年M月d日 HH:mm").string(from: self.scheduleData.date)
        self.memberCountLabel.text = "(\(self.members.count)名)"
        self.providerLabel.text = self.scheduleData.provider
        self.descriptionLabel.set(text: self.scheduleData.description, lineHeight: 16)

        self.timerProc()
        Timer.scheduledTimer(withTimeInterval: 1, repeats: true, block: { _ in
            self.timerProc()
        })
        
        DispatchQueue.main.asyncAfter(deadline: .now() + 2) {
            if !SaveData.shared.sentFirstMessageScheduleIds.contains(self.scheduleData.id) {
                self.stackMatch()
            } else if self.scheduleData.date <= Date() {
                self.stackAttend()
            }
        }
    }
    
    private func getInitialInfo() -> (matchUserData: UserData, tableId: String)? {
        
        var targetIndex = -1
        var tableIndex = -1
        
        guard let index = self.members.index(where: { $0.userId == SaveData.shared.userId }) else {
            return nil
        }
        if index % 2 == 1 {
            targetIndex = index - 1
            tableIndex = Int((index - 1) / 2)
        } else {
            if index + 1 >= self.members.count {
                if self.members.count >= 2 {
                    targetIndex = index - 1
                    tableIndex = Int((index - 1) / 2)
                } else {
                    return nil
                }
            } else {
                targetIndex = index + 1
                tableIndex = Int(index / 2)
            }
        }
        return (matchUserData: self.members[targetIndex], tableId: String(format: "%d", tableIndex))
    }
    
    private func stackMatch() {

        guard let initialInfo = self.getInitialInfo() else {
            return
        }
        let match = self.viewController(storyboard: "Attend", identifier: "AttendMatchViewController") as! AttendMatchViewController
        match.set(scheduleData: self.scheduleData, userData: initialInfo.matchUserData, tableId: initialInfo.tableId, completion: { [weak self] in
            if let scheduleDate = self?.scheduleData.date, scheduleDate >= Date() {
                self?.stackAttend()
            }
        })
        self.stack(viewController: match, animationType: .none)
    }
    
    private func stackAttend() {
        
        let blackView = UIView()
        blackView.backgroundColor = .black
        blackView.alpha = 0
        self.view.addSubview(blackView)
        blackView.translatesAutoresizingMaskIntoConstraints = false
        blackView.topAnchor.constraint(equalTo: self.view.topAnchor).isActive = true
        blackView.leadingAnchor.constraint(equalTo: self.view.leadingAnchor).isActive = true
        blackView.trailingAnchor.constraint(equalTo: self.view.trailingAnchor).isActive = true
        blackView.bottomAnchor.constraint(equalTo: self.view.bottomAnchor).isActive = true
        
        UIView.animate(withDuration: 0.2, animations: {
            blackView.alpha = 1.0
        }, completion: { [weak self] _ in
            guard let scheduleData = self?.scheduleData, let initialInfo = self?.getInitialInfo() else {
                return
            }
            let attend = self?.viewController(storyboard: "Attend", identifier: "AttendViewController") as! AttendViewController
            attend.set(scheduleData: scheduleData, initialTableId: initialInfo.tableId)
            self?.tabbarViewController()?.stack(viewController: attend, animationType: .none)
            self?.view.bringSubview(toFront: blackView)
            
            self?.pop(animationType: .none)
            
            UIView.animate(withDuration: 0.2, animations: {
                blackView.alpha = 0
            }, completion: { _ in
                blackView.removeFromSuperview()
            })
        })
    }
    
    private func timerProc() {
        
        let remainTime = self.scheduleData.date.timeIntervalSinceNow
        if remainTime > 0 {
            let remainMinutes = String(format: "%02d", Int(remainTime / 60))
            let remainSeconds = String(format: "%02d", Int(remainTime) % 60)
            self.remainTimeLabel.text = remainMinutes + ":" + remainSeconds
        } else {
            self.remainTimeTitleLabel.text = ""
            self.remainTimeLabel.text = "開催中"
        }
    }
    
    @IBAction func onTapClose(_ sender: Any) {
        self.pop(animationType: .vertical)
    }
}

extension AttendPrepareViewController: UICollectionViewDelegate, UICollectionViewDataSource {
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return self.members.count
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "AttendPrepareMemberCollectionViewCell", for: indexPath) as! AttendPrepareMemberCollectionViewCell
        cell.configure(userData: self.members[indexPath.row])
        return cell
    }
}
