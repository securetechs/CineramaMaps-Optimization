//
//  MarkerImageCache.swift
//  Cinerama Maps
//

import UIKit

class MarkerImageCache {
    static let shared = MarkerImageCache()
    private let cache = NSCache<NSString, UIImage>()
    
    private init() {
        cache.countLimit = 500 // Limit number of markers in memory
    }
    
    func getImage(for key: String) -> UIImage? {
        return cache.object(forKey: key as NSString)
    }
    
    func setImage(_ image: UIImage, for key: String) {
        cache.setObject(image, forKey: key as NSString)
    }
    
    func clear() {
        cache.removeAllObjects()
    }
    
    func generateKey(placeId: String, isZoomed: Bool, fav: Bool, colors: [UIColor], onlyIcon: Bool, name: String?) -> String {
        let colorKeys = colors.map { $0.toHexString() }.joined(separator: ",")
        return "\(placeId)_\(isZoomed ? "z" : "nz")_\(fav ? "f" : "nf")_\(colorKeys)_\(onlyIcon ? "oi" : "all")_\(name ?? "")"
    }
}

extension UIColor {
    func toHexString() -> String {
        var r: CGFloat = 0, g: CGFloat = 0, b: CGFloat = 0, a: CGFloat = 0
        getRed(&r, green: &g, blue: &b, alpha: &a)
        return String(format: "#%02X%02X%02X", Int(r * 255), Int(g * 255), Int(b * 255))
    }
}
