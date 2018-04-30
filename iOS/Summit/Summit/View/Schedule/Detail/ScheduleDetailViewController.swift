//
//  ScheduleDetailViewController.swift
//  Summit
//
//  Created by Leapfrog-Software on 2018/04/30.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

class ScheduleDetailViewController: UIViewController {

    private var scheduleData: ScheduleData!
    private var members = [UserData]()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.members = UserRequester.shared.dataList.filter { userData -> Bool in
            return userData.reserves.contains(self.scheduleData.id)
        }
    }
    
    func set(scheduleData: ScheduleData) {
        self.scheduleData = scheduleData
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
