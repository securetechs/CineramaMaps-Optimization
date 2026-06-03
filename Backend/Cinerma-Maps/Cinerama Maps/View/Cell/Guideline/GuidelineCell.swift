//
//  GuidelineCell.swift
//  Cinerama Maps
//
//  Created by Techimmense Software Solutions on 22/08/24.
//

import UIKit
import SkeletonView

class GuidelineCell: UICollectionViewCell {

    @IBOutlet weak var img: UIImageView!
    @IBOutlet weak var lbl_Text: UILabel!
    @IBOutlet weak var moreText: UILabel!
//    @IBOutlet weak var btn_MoreOt: UIButton!
    
//    var cloMore:(() -> Void)?
    
    override func awakeFromNib() {
        super.awakeFromNib()
        
        // MARK: Enable Skeleton
        isSkeletonable = true
        contentView.isSkeletonable = true
        
        // Main container view (the one with shadow/corner radius in XIB)
        if let containerView = contentView.subviews.first {
            containerView.isSkeletonable = true
        }
        
        // Enable skeleton for specific outlets
        img.isSkeletonable = true
        lbl_Text.isSkeletonable = true
        moreText.isSkeletonable = true
        
        setupSkeletonUI()
        
        self.img.roundCorners(corners: [.topLeft, .topRight], radius: 10)
    }
    
    private func setupSkeletonUI() {
        // Image skeleton: match the corner radius and add padding
        img.skeletonCornerRadius = 10
//        img.skeletonPaddingInsets = UIEdgeInsets(top: 0, left: 5, bottom: 5, right: 5)
        
        // Label skeleton: set fixed ratio and add padding to prevent overflow
        lbl_Text.linesCornerRadius = 6
        lbl_Text.skeletonTextLineHeight = .relativeToFont
        lbl_Text.lastLineFillPercent = 60
        lbl_Text.skeletonLineSpacing = 6
        lbl_Text.skeletonPaddingInsets = UIEdgeInsets(top: 0, left: 5, bottom: 5, right: 5)
        
        moreText.linesCornerRadius = 6
        moreText.skeletonTextLineHeight = .relativeToFont
        moreText.lastLineFillPercent = 100
        moreText.skeletonPaddingInsets = UIEdgeInsets(top: 0, left: 5, bottom: 5, right: 5)
        
        // More button: set a fixed rounded appearance and add padding
//        btn_MoreOt.skeletonCornerRadius = 16
//        btn_MoreOt.titleLabel?.skeletonPaddingInsets = UIEdgeInsets(top: 0, left: 5, bottom: 0, right: 5)
    }
    
//    @IBAction func btn_More(_ sender: UIButton) {
//        self.cloMore?()
//    }
}
