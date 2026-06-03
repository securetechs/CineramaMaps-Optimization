//
//  WebViewCacheManager.swift
//  Cinerama Maps
//
//  Created by Techimmense Software Solutions on 28/08/24.
//

import Foundation

class WebViewCacheManager {
    
    static let shared = WebViewCacheManager()
    
    private let cacheDirectory: URL
    
    private init() {
        let paths = FileManager.default.urls(for: .cachesDirectory, in: .userDomainMask)
        cacheDirectory = paths[0].appendingPathComponent("WebViewHTMLCache")
        
        // Create directory if it doesn't exist
        if !FileManager.default.fileExists(atPath: cacheDirectory.path) {
            try? FileManager.default.createDirectory(at: cacheDirectory, withIntermediateDirectories: true, attributes: nil)
        }
    }
    
    /// ✅ Save HTML string to disk
    func save(html: String, forKey key: String) {
        let fileURL = cacheDirectory.appendingPathComponent(key.safeFileName())
        do {
            try html.write(to: fileURL, atomically: true, encoding: .utf8)
        } catch {
            print("❌ Error saving HTML cache: \(error)")
        }
    }
    
    /// ✅ Load HTML string from disk
    func load(forKey key: String) -> String? {
        let fileURL = cacheDirectory.appendingPathComponent(key.safeFileName())
        return try? String(contentsOf: fileURL, encoding: .utf8)
    }
    
    /// ✅ Clear specific cache
    func clear(forKey key: String) {
        let fileURL = cacheDirectory.appendingPathComponent(key.safeFileName())
        try? FileManager.default.removeItem(at: fileURL)
    }
}

fileprivate extension String {
    func safeFileName() -> String {
        return self.components(separatedBy: .punctuationCharacters).joined(separator: "_")
            .components(separatedBy: .whitespacesAndNewlines).joined(separator: "_")
            .appending(".html")
    }
}
