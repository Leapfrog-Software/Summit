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
    @IBOutlet private weak var remainTimeLabel: UILabel!
    @IBOutlet private weak var dateLabel: UILabel!
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
        self.providerLabel.text = self.scheduleData.provider
        self.descriptionLabel.set(text: self.scheduleData.description, lineHeight: 16)

        self.timerProc()
        Timer.scheduledTimer(withTimeInterval: 1, repeats: true, block: { _ in
            self.timerProc()
        })
        
        if !SaveData.shared.sentFirstMessageScheduleIds.contains(self.scheduleData.id) {
            DispatchQueue.main.asyncAfter(deadline: .now() + 2) {
                let match = self.viewController(storyboard: "Attend", identifier: "AttendMatchViewController") as! AttendMatchViewController
                self.stack(viewController: match, animationType: .none)
            }
        }
    }
    
    private func timerProc() {
        
        let remainTime = self.scheduleData.date.timeIntervalSinceNow
        let remainMinutes = Int(remainTime / 60)
        let remainSeconds = Int(remainTime) % 60
        self.remainTimeLabel.text = "\(remainMinutes):\(remainSeconds)"
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
