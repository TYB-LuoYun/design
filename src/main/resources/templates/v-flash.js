//
// .v-flash-anim-yellow {
//   animation: flashYellow 1s ease;
// }
//
// .v-flash-anim-green {
//   animation: flashGreen 1s ease;
// }
//
// .v-flash-anim-red {
//   animation: flashRed 1s ease;
// }
//
// @keyframes flashYellow {
//   0% { background-color: #fff3cd; }
//   50% { background-color: #ffeeba; }
//   100% { background-color: transparent; }
// }
//
// @keyframes flashGreen {
//   0% { background-color: #d4edda; }
//   50% { background-color: #a3d7a5; }
//   100% { background-color: transparent; }
// }
//
// @keyframes flashRed {
//   0% { background-color: #f8d7da; }
//   50% { background-color: #e09b9b; }
//   100% { background-color: transparent; }
// }
//





function triggerFlash(el, colorClass) {
  el.classList.remove('v-flash-anim-red', 'v-flash-anim-green', 'v-flash-anim-yellow');
  void el.offsetWidth; // 触发重绘，重启动画
  if (colorClass) {
    el.classList.add(colorClass);
  } else {
    el.classList.add('v-flash-anim-yellow'); // 默认黄色闪
  }
  setTimeout(() => {
    el.classList.remove('v-flash-anim-red', 'v-flash-anim-green', 'v-flash-anim-yellow');
  }, 1000);
}

function isEqual(a, b) {   
   return JSON.stringify(a) === JSON.stringify(b);
}

function clone(val) {
  return typeof val === 'object' ? JSON.parse(JSON.stringify(val)) : val;
}

/**
 *  如果页面切换不能改变，需要传入页码
 * v-flash="{ data: row.productQuantity, page: queryParams.pageNum }"
 */

export default function(el, binding) {
  const newVal = binding.value;
  const oldVal = el._vFlashPrev;
  const colorParam = binding.arg;  // 这里拿到冒号后面的参数，比如 green 或 red  
  // 跳过首次绑定（oldVal 为 undefined）
  if (oldVal === undefined) {
    el._vFlashPrev = clone(newVal);
    console.log("跳过首次绑定")
    return;
  } 
  
  let isChange = false;
  if(typeof oldVal === 'object' && oldVal.page != newVal.page){ 
    // 说明更换了页码，更换了页码后，如果值一样
    // console.log("更换了页码") 
    el.pageChange = true
  } else if(typeof oldVal === 'object' && oldVal.page == newVal.page){
    // console.log("页码没变,检测值变化") 
    if(!el.pageChange){
      // console.log("新值"+newVal.data) 
      // console.log("旧值"+el.data) 
      isChange = true
    }
    el.pageChange = false 
  }else{
    isChange = true
  }


  if (oldVal !== undefined &&isChange&& !isEqual(oldVal, newVal)) {
    if (colorParam === 'yellow') {
      triggerFlash(el);
    }else if (colorParam === 'green') {
      triggerFlash(el, 'v-flash-anim-green');
    } else if (colorParam === 'red') {
      triggerFlash(el, 'v-flash-anim-red');
    } else {
      let newData = newVal;
      let oldData = oldVal;
      if(typeof oldVal === 'object'){
          newData = newData.data
          oldData = oldData.data
      }
      // 没参数就自动判断数字增减
      if (typeof newData === 'number' && typeof oldData === 'number') {
        
        if ( newData > oldData) {
          triggerFlash(el, 'v-flash-anim-green');
        } else if ( newData < oldData) {
          triggerFlash(el, 'v-flash-anim-red');
        } else {
          triggerFlash(el); // 值没变，不闪，这里保底黄闪
        }
      } else {
        triggerFlash(el); // 非数字默认黄闪
      }
    }
    
  } 

   el._vFlashPrev = clone(newVal) 
  

  
}
