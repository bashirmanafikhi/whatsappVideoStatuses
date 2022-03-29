using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace WebApplication3.Models
{
    public class MyVideo
    {
        public string VideoKey { get; set; }
        public string VideoUrl { get; set; }
        public string VideoTitle { get; set; }
        public string Category { get; set; }
        public string ImageUrl { get; set; }
        public int LikesCount { get; set; }
        public int ViewsCount { get; set; }
        public int DownloadsCount { get; set; }
    }
}