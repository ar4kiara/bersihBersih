package id.ar4kiara.storageinspector.ui

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import coil3.compose.AsyncImage
import id.ar4kiara.storageinspector.data.FileEntity
import id.ar4kiara.storageinspector.util.asFileSize

@Composable fun MediaPreviewDialog(file:FileEntity,onDismiss:()->Unit,onDelete:()->Unit){Dialog(onDismissRequest=onDismiss){Surface(shape=MaterialTheme.shapes.large,modifier=Modifier.fillMaxWidth().fillMaxHeight(.86f)){Column(Modifier.fillMaxSize()){Box(Modifier.weight(1f).fillMaxWidth().background(MaterialTheme.colorScheme.surfaceContainerHighest),contentAlignment=Alignment.Center){when{file.mime?.startsWith("image/")==true->AsyncImage(model=file.uri,contentDescription=file.name,modifier=Modifier.fillMaxSize());file.mime?.startsWith("video/")==true||file.mime?.startsWith("audio/")==true->PlayerPreview(file.uri);else->Column(horizontalAlignment=Alignment.CenterHorizontally){Icon(Icons.Outlined.Info,null,Modifier.size(64.dp));Text("Preview tidak tersedia untuk tipe ini")}}};Column(Modifier.padding(16.dp),verticalArrangement=Arrangement.spacedBy(6.dp)){Text(file.name,style=MaterialTheme.typography.titleMedium);Text("${file.size.asFileSize()} • ${file.category} • ${file.source}",style=MaterialTheme.typography.bodySmall);Text(file.parent,style=MaterialTheme.typography.labelSmall);Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.spacedBy(8.dp)){OutlinedButton(onClick=onDismiss,modifier=Modifier.weight(1f)){Text("Keep")};Button(onClick=onDelete,modifier=Modifier.weight(1f)){Icon(Icons.Outlined.Delete,null);Spacer(Modifier.width(6.dp));Text("Delete")}}}}}}}
@Composable private fun PlayerPreview(uri:String){val context=LocalContext.current;val player=remember(uri){ExoPlayer.Builder(context).build().apply{setMediaItem(MediaItem.fromUri(Uri.parse(uri)));prepare()}};DisposableEffect(player){onDispose{player.release()}};AndroidView(factory={PlayerView(it).apply{this.player=player;useController=true}},modifier=Modifier.fillMaxSize())}
