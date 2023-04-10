
let currentBlockIndex = 0; // Initialize CurrentBlockIndex

window.smoothScroll = function(target) {
  var scrollContainer = target;
  do { //find scroll container
      scrollContainer = scrollContainer.parentNode;
      console.log(scrollContainer)
      if (!scrollContainer) return;
      scrollContainer.scrollTop += 1;
  } while (scrollContainer.scrollTop == 0);

  var blocks = target.parentElement.children; // get all child elements of parent element
  for (var i = 0; i < blocks.length; i++) { // loop through all child elements
    if (blocks[i] === target) { // check if child element is target
      CurrentBlockIndex = i; // set current block index
      break;
    }
  }
  
  var targetY = 0;
  do { //find the top of target relatively to the container
      if (target == scrollContainer) break;
      targetY += target.offsetTop;
  } while (target = target.offsetParent);
  
  scroll = function(c, a, b, i) {
      i++; if (i > 30) return;
      c.scrollTop = a + (b - a) / 30 * i;
      setTimeout(function(){ 
        scroll(c, a, b, i);
        var newBlockIndex = Math.round(c.scrollTop / (c.scrollHeight / blocks.length)); // calculate new block index
        if (newBlockIndex !== currentBlockIndex) { // check if current block index has changed
          currentBlockIndex = newBlockIndex; // update current block index
        }
      }, 5);
  }
  // start scrolling
  scroll(scrollContainer, scrollContainer.scrollTop, targetY, 0);
}

const md = new markdownit();

$.ajax({
  url: "./VISION.md",
  success: function(markdownText) {
    const html = md.render(markdownText);

    const headingRegex = /<h2>(.*)<\/h2>/g;
    const matches = html.matchAll(headingRegex);

    const sections = {};

    for (const match of matches) {
      const title = match[1];
      const start = match.index;
      let end = html.length;

      const nextMatch = matches.next().value;
      if (nextMatch) {
        end = nextMatch.index;
      }

      const content = html.slice(start, end);

      sections[title] = content;
      console.log(sections)
    }

    for (const [title, content] of Object.entries(sections)) {
      const sectionElements = document.querySelectorAll(`[data-section="${title}"]`);

      for (const sectionElement of sectionElements) {
        sectionElement.innerHTML = content;
      }
    }
  }
});