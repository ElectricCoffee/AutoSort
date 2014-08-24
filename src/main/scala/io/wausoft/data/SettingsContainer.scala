package io.wausoft.data

/* Example of a settings file:
 * {
 *   "autosort-folder-path": "C:\Users\ElectricCoffee\Desktop\AutoSort",
 *   "local-settings": [{
 *     "file-types": ["iso", "dmg"],
 *     "destination-path": "C:\Users\ElectricCoffee\DiskImages",
 *     "keywords": []
 *   }]
 * }
 */

case class SettingsContainer(`autosort-folder-path`: String, `local-settings`: List[LocalSettingsContainer])
case class LocalSettingsContainer(`file-types`: List[String], `destination-path`: String, keywords: List[String])
