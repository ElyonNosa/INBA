
window.smoothScroll = function(target) {
  var scrollContainer = target;
  do { //find scroll container
      scrollContainer = scrollContainer.parentNode;
      console.log(scrollContainer)
      if (!scrollContainer) return;
      scrollContainer.scrollTop += 1;
  } while (scrollContainer.scrollTop == 0);
  
  var targetY = 0;
  do { //find the top of target relatively to the container
      if (target == scrollContainer) break;
      targetY += target.offsetTop;
  } while (target = target.offsetParent);
  
  scroll = function(c, a, b, i) {
      i++; if (i > 30) return;
      c.scrollTop = a + (b - a) / 30 * i;
      setTimeout(function(){ scroll(c, a, b, i); }, 5);
  }
  // start scrolling
  scroll(scrollContainer, scrollContainer.scrollTop, targetY, 0);
}

// sub-block shifter

const scrollingWrapper = document.querySelector('.sub-block-container');
const cards = document.querySelectorAll('.sub-block');
const prevBtn = document.getElementById('prevBtn');
const nextBtn = document.getElementById('nextBtn');
const cardWidth = $(document).width();
const numCards = cards.length;
let currentPosition = 0;

function scrollCards(direction) {
  currentPosition += direction * cardWidth;
  currentPosition = Math.max(currentPosition, 0);
  currentPosition = Math.min(currentPosition, (numCards - 1) * cardWidth);
  scrollingWrapper.scrollTo(currentPosition, 0);
}

prevBtn.disabled = true;
if (numCards <= 1) {
  nextBtn.disabled = true;
}

scrollingWrapper.addEventListener('scroll', function() {
  currentPosition = scrollingWrapper.scrollLeft;
  prevBtn.disabled = currentPosition === 0;
  nextBtn.disabled = currentPosition === (numCards - 1) * cardWidth;
});

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
    }

    for (const [title, content] of Object.entries(sections)) {
      const sectionElements = document.querySelectorAll(`[data-section="${title}"]`);

      for (const sectionElement of sectionElements) {
        sectionElement.innerHTML = content;
      }
    }
  }
});