package id.ar4kiara.storageinspector.util

import java.util.Locale
fun Long.asFileSize():String{if(this<1024)return "$this B";val units=arrayOf("KB","MB","GB","TB");var v=toDouble();var i=-1;do{v/=1024.0;i++}while(v>=1024&&i<units.lastIndex);return String.format(Locale.US,if(v>=100)"%.0f %s" else "%.1f %s",v,units[i])}
