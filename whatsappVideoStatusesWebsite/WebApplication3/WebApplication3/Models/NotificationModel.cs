using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Web;

namespace WebApplication3.Models
{
    public class NotificationModel
    {
        [DataType(DataType.Password)]
        [Required(ErrorMessage = "كول خرا ودخل كلمة السر")]
        [Display(Name = "Password")]
        public string Password { get; set; }

        [DataType(DataType.Text)]
        [Required(ErrorMessage = "دخل العنوان تلحس طيزي")]
        [Display(Name = "Title")]
        public string Title { get; set; }

        [DataType(DataType.Text)]
        [Required(ErrorMessage = "كتوب الإشعار يا حيوان")]
        [Display(Name = "Password")]
        public string Body { get; set; }


        [Display(Name = "Badge")]
        [Range(0, 100)]
        public int Badge { get; set; }
    }
}