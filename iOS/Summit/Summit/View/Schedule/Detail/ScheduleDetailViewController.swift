//
//  ScheduleDetailViewController.swift
//  Summit
//
//  Created by Leapfrog-Software on 2018/04/30.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

class ScheduleDetailViewController: UIViewController {
    
    @IBOutlet private weak var scheduleImageView: UIImageView!
    @IBOutlet private weak var nameLabel: UILabel!
    @IBOutlet private weak var memberCountLabel: UILabel!
    @IBOutlet private weak var dateLabel: UILabel!
    @IBOutlet private weak var providerLabel: UILabel!
    @IBOutlet private weak var descriptionLabel: UILabel!
    @IBOutlet private weak var filterLabel: UILabel!

    private var scheduleData: ScheduleData!
    private var members = [UserData]()
    
    func set(scheduleData: ScheduleData) {
        self.scheduleData = scheduleData
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.initContents()
    }
    
    private func initContents() {

        self.members = UserRequester.shared.dataList.filter { userData -> Bool in
            return userData.reserves.contains(self.scheduleData.id)
        }
        
        ImageStorage.shared.fetch(url: Constants.ScheduleImageDirectory + self.scheduleData.id, imageView: self.scheduleImageView)
        
        self.nameLabel.text = self.scheduleData.title
        
        let memberCount = self.members.count
        self.memberCountLabel.text = "(\(memberCount)名)"
        
        self.dateLabel.text = DateFormatter(dateFormat: "M月d日 H:m〜").string(from: self.scheduleData.date)
        
        self.providerLabel.text = self.scheduleData.provider
        
        self.descriptionLabel.set(text: self.scheduleData.description, lineHeight: 18)
        self.filterLabel.text = self.scheduleData.filter
    }
    
    @IBAction func onTapReserve(_ sender: Any) {
        
        if var myUserData = UserRequester.shared.myUserData() {
            myUserData.reserves.append(self.scheduleData.id)
            AccountRequester.updateUser(userData: myUserData, completion: { result in
                // TODO
            })
        }
    }
    
    @IBAction func onTapClose(_ sender: Any) {
        self.pop(animationType: .horizontal)
    }
}

extension ScheduleDetailViewController: UICollectionViewDelegate, UICollectionViewDataSource {
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return self.members.count
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "ScheduleDetailMemberCollectionViewCell", for: indexPath) as! ScheduleDetailMemberCollectionViewCell
        cell.configure(userData: self.members[indexPath.row])
        return cell
    }
}
