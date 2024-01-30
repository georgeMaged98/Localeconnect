import {Component, OnInit} from '@angular/core';
import {FeedService} from "../../service/feed.service";
import {Post} from "../../model/feed";

@Component({
  selector: 'app-feed',
  templateUrl: './feed.component.html',
  styleUrls: ['./feed.component.scss']
})
export class FeedComponent implements OnInit {
  posts: Post[] = [];
  followers: any[] = [];

  constructor(private feedService: FeedService) {
  }

  ngOnInit(): void {
    this.fetchPosts();
  }

  fetchPosts(): void {
    this.feedService.getPosts().subscribe({
        next: (data: Post[]) => {
          this.posts = data;
        },
        error: (err) => {
          console.error(err);
        }
      }
    );
  }

}



