package com.example.smallandroidmediaplayerapp.data.repository

import android.content.ContentUris
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import com.example.smallandroidmediaplayerapp.domain.model.AudioFile
import com.example.smallandroidmediaplayerapp.domain.repository.AudioRepository

class AudioRepositoryImpl(
    private val context: Context,
) : AudioRepository {

    // get all audio files from devise
    override suspend fun getAllAudioFiles(): List<AudioFile> {
        val audioList = mutableListOf<AudioFile>()

        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.ALBUM_ID
        )

      // Filter files to answer only mp3 or wav
        val selection =
            "${MediaStore.Audio.Media.MIME_TYPE}=? OR ${MediaStore.Audio.Media.MIME_TYPE}=?"
        val selectionArgs = arrayOf("audio/mpeg", "audio/wav")

        val query = context.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            "${MediaStore.Audio.Media.TITLE} ASC"
        )

        query?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val titleColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            val artistColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
            val durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
            val albumIdColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val title = cursor.getString(titleColumn)
                val artist = cursor.getString(artistColumn)
                val duration = cursor.getLong(durationColumn)
                val albumId = cursor.getLong(albumIdColumn)

                val contentUri = ContentUris.withAppendedId(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    id
                )

                val albumArt = getAlbumArt(albumId)

                audioList.add(
                    AudioFile(
                        id = id,
                        title = title,
                        artist = artist,
                        duration = duration,
                        uri = contentUri.toString(),
                        albumArt = albumArt
                    )
                )
            }
        }

        return audioList
    }

    // Get album art
    private fun getAlbumArt(albumId: Long): Bitmap? {
        return try {
            val albumArtUri = ContentUris.withAppendedId(
                Uri.parse("content://media/external/audio/albumart"),
                albumId
            )
            context.contentResolver.openInputStream(albumArtUri)?.use { inputStream ->
                BitmapFactory.decodeStream(inputStream)
            }
        } catch (e: Exception) {
            null
        }
    }
}
