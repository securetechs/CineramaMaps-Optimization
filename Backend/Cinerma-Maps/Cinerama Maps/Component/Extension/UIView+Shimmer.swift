//
//  UIView+Shimmer.swift
//  Cinerama Maps
//
//  Created by Gemini on 29/04/26.
//

import UIKit

extension UIView {
    
    func startShimmering() {
        let light = UIColor(white: 0.9, alpha: 1.0).cgColor
        let dark = UIColor(white: 0.8, alpha: 1.0).cgColor
        
        let gradient = CAGradientLayer()
        gradient.colors = [dark, light, dark]
        gradient.frame = CGRect(x: -self.bounds.width, y: 0, width: 3 * self.bounds.width, height: self.bounds.height)
        gradient.startPoint = CGPoint(x: 0.0, y: 0.5)
        gradient.endPoint = CGPoint(x: 1.0, y: 0.5)
        gradient.locations = [0.4, 0.5, 0.6]
        gradient.name = "shimmerLayer"
        self.layer.addSublayer(gradient)
        
        let animation = CABasicAnimation(keyPath: "locations")
        animation.fromValue = [0.0, 0.1, 0.2]
        animation.toValue = [0.8, 0.9, 1.0]
        animation.duration = 1.2
        animation.repeatCount = .infinity
        gradient.add(animation, forKey: "shimmer")
    }
    
    func stopShimmering() {
        self.layer.sublayers?.forEach {
            if $0.name == "shimmerLayer" {
                $0.removeFromSuperlayer()
            }
        }
    }
    
    func startShimmeringRecursively() {
        if self is UILabel || self is UIImageView {
            self.startShimmering()
        }
        for subview in subviews {
            subview.startShimmeringRecursively()
        }
    }
    
    func stopShimmeringRecursively() {
        self.stopShimmering()
        for subview in subviews {
            subview.stopShimmeringRecursively()
        }
    }
}
