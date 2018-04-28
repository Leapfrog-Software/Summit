//
//  UIImage+Crop.swift
//  Summit
//
//  Created by Leapfrog-Software on 2018/04/29.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

extension UIImage {
    
    func crop(size: CGSize) -> UIImage? {
        
        guard let trimmed = self.trim() else {
            return nil
        }
        return trimmed.resize(to: size)
    }
    
    private func trim() -> UIImage? {
        
        var rect = CGRect.zero
        if self.size.width > self.size.height {
            rect = CGRect(x: (self.size.width - self.size.height) / 2,
                          y: 0,
                          width: self.size.height,
                          height: self.size.height)
        } else {
            rect = CGRect(x: 0,
                          y: (self.size.height - self.size.width) / 2,
                          width: self.size.width,
                          height: self.size.width)
        }
        if let cgImage = self.cgImage?.cropping(to: rect) {
            return UIImage(cgImage: cgImage)
        } else {
            return nil
        }
    }
    
    private func resize(to: CGSize) -> UIImage? {
        
        UIGraphicsBeginImageContext(to)
        self.draw(in: CGRect(origin: .zero, size: to))
        let resizedImage = UIGraphicsGetImageFromCurrentImageContext()
        UIGraphicsEndImageContext()
        
        return resizedImage
    }
}
