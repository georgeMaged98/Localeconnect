import {ChangeDetectorRef, Component} from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import {ImagesService} from "../../../service/image.service";
import {FeedService} from "../../../service/feed.service";
import {Post} from "../../../model/feed";

@Component({
  selector: 'app-add-post-dialog',
  templateUrl: './add-post-dialog.component.html',
  styleUrls: ['./add-post-dialog.component.scss']
})
export class AddPostDialogComponent {
  postForm: FormGroup;
  images: string[] = [];
  showEmojiPicker = false;

  constructor(private cdr: ChangeDetectorRef,
    private feedService: FeedService,
    public dialogRef: MatDialogRef<AddPostDialogComponent>,
    private formBuilder: FormBuilder,
    private imageService: ImagesService
  ) {
    this.postForm = this.formBuilder.group({
      content: ['', Validators.required],
      images: ['']
    });
  }

  onNoClick(): void {
    this.dialogRef.close();
  }

  onSubmit(): void {
    if (this.postForm.valid) {
      const postData = { ...this.postForm.value, images: this.images };
      const post: Post={
        id:0,//TODO: generate from backend
        author:{
          userId: 1,
          name: 'Alice Johnson',
          username: 'alicej',
          isFollowing: true,
          profileImage: 'https://www.profilebakery.com/wp-content/uploads/2023/04/AI-Profile-Picture.jpg',
        } ,//TODO: get current user
        content:postData.content,
        images: this.images,
        comments:[],
        date: new Date()
      }
      //TODO: add api call
     this.feedService.changePost(post);
      this.dialogRef.close(postData);
    }
  }

  toggleEmojiPicker() {
    this.showEmojiPicker = !this.showEmojiPicker;
  }

  addEmoji(event: any) {
    const emoji = event.emoji.native;
    const currentContent: string= this.postForm.value.content;
    this.postForm.controls['content'].setValue(currentContent + emoji);
    this.showEmojiPicker=false;
    this.cdr.detectChanges();
  }

  onFileSelected(event: any) {
    const files = event.target.files;
    if (files) {
      Array.from(files).forEach((file) => {
        const reader = new FileReader();
        reader.onload = (e: any) => {
          this.images.push(e.target.result);
          if (this.images.length === files.length) {
            this.imageService.updateImages(this.images); // Assuming imageService handles image array updates
          }
        };
        reader.readAsDataURL(<Blob>file);
      });
    }
  }
}
