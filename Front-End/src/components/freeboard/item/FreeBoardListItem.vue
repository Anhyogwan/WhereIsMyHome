<template>
  <tr>
    <td>{{ articleno }}</td>
    <th>{{ subject }}</th>
    <td>{{ hit }}</td>
    <td>{{ userid }}</td>
    <td>{{ regtime }}</td>
  </tr>
</template>

<script>
export default {
  name: "FreeBoardListItem",
  props: {
    articleno: Number,
    userid: String,
    subject: String,
    hit: Number,
    regtime: String,
  },
};
</script>

<style></style>
